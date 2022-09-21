package com.kob.backend.consumer;

/**
 * @author :王冰冰
 * @date : 2022/9/18
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // 线程安全的map
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); // 对所有实例可见，全局变量
    private User user;
    private Session session = null;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private Game game = null;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    // 匹配池, 线程安全的set
    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接, 身份的验证
        this.session = session;
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        if (this.user != null) {
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("received message!");
        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if (Objects.equals(event, "start-matching")) {
            startMatching();
        } else if (Objects.equals(event, "stop-matching")) {
            stopMatching();
        } else if (Objects.equals(event, "move")) {
            move(data.getInteger("direction"));
        }
    }

    private void startMatching() {
        System.out.println("start matching!");
        matchPool.add(this.user);

        while (matchPool.size() >= 2) {
            Iterator<User> it = matchPool.iterator();
            User a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);
            Game game = new Game(13, 14, 16, a.getId(), b.getId());
            game.createMap();
            game.start();
            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;
            JSONObject respA = getResMessage(b, getStartLocation(game));
            users.get(a.getId()).sendMessage(respA.toJSONString());
            JSONObject respB = getResMessage(a, getStartLocation(game));
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private JSONObject getResMessage(User x, JSONObject game) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "start-matching");
        jsonObject.put("opponent_username", x.getUserName());
        jsonObject.put("opponent_photo", x.getPhoto());
        jsonObject.put("game", game);
        return jsonObject;
    }

    private JSONObject getStartLocation (Game game) {
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());
        return respGame;
    }


    private void stopMatching() {
        System.out.println("stop matching!");
        matchPool.remove(this.user);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
