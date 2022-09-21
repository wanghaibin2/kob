package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author :王冰冰
 * @date : 2022/9/18
 */
public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing -> finished
    private String loser = ""; // all -> 平局

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer userIdA, Integer userIdB) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[rows][cols];
        playerA = new Player(userIdA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(userIdB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i ++) {
            int nx = sx + dx[i];
            int ny = sy + dy[i];
            if (nx >= 0 && nx < this.rows && ny >= 0 && ny <= this.cols && g[nx][ny] == 0) {
                if (check_connectivity(nx, ny, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public boolean drawMap() { // 画地图
        for (int i = 0; i < this.rows; i ++) {
            for (int j = 0; j < this.cols; j ++) {
                g[i][j] = 0;
            }
        }
        for (int r = 0; r < this.rows; r ++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i ++) {
            for (int j = 0; j < 1000; j ++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2)) {
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++) {
            if(drawMap()) {
                break;
            }
        }
    }

    public String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                res.append(g[i][j]);
            }
        }
            return res.toString();
    }

    private boolean nextStep() { // 等待两名玩家的下一步操作
        try {
            Thread.sleep(200); // 前端设定的一秒走5格
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 50; i ++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        sendAllMessage(resp.toJSONString());
        savePkRecord();
    }

    private void judge() { // 判断两名玩家下一步操作是否合法
        boolean validA = check_valid(playerA.getCells(), playerB.getCells());
        boolean validB = check_valid(playerB.getCells(), playerA.getCells());
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private boolean check_valid(List<Cell> cellListA, List<Cell> cellListB) {
        int lenA = cellListA.size();
        Cell cell = cellListA.get(lenA - 1);
        if (g[cell.getX()][cell.getY()] == 1) return false;
        for (int i = 0; i < lenA - 1; i ++) {
            if (Objects.equals(cellListA.get(i).getX(), cell.getX()) && Objects.equals(cellListA.get(i).getY(), cell.getY())) {
                return false;
            }
        }

        for (int i = 0; i < lenA - 1; i ++) {
            if (Objects.equals(cellListB.get(i).getX(), cell.getX()) && Objects.equals(cellListB.get(i).getY(), cell.getY())) {
                return false;
            }
        }

        return true;
    }

    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;  // 清空之前的操作
        } finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void savePkRecord() {
        Record record = new Record(null, playerA.getId(), playerA.getSx(), playerA.getSy(), playerB.getId(), playerB.getSx(), playerB.getSy(),
                playerA.getStepsString(), playerB.getStepsString(), getMapString() , loser, new Date(), new Date());
        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++) {
            if (nextStep()) {
                judge();
                if (Objects.equals(status, "playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else if (nextStepB == null) {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
