package com.kob.matchingSystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
@Component
public class MatchingPool extends Thread {
    private static List<Player> playerList = new ArrayList<>();
    private static RestTemplate restTemplate = new RestTemplate();

    private final static String  startGameUrl = "http://127.0.0.1:3000/pk/start/game";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    private ReentrantLock lock = new ReentrantLock();

    public void addPlayer(Integer userId, Integer rating) {
        lock.lock();
        try {
            playerList.add(new Player(userId, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            playerList = playerList.stream().filter(item -> !Objects.equals(item.getUserId(), userId)).collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {  // 等待时间加一
        for (Player it : playerList) {
            it.setWaitingTime(it.getWaitingTime() + 1);
        }
    }



    private boolean checkMatched(Player playerA, Player playerB) {
        int ratingDelta = Math.abs(playerA.getRating() - playerB.getRating());
        int minWaitingTime = Math.min(playerA.getWaitingTime(), playerB.getWaitingTime());
        return ratingDelta <= minWaitingTime * 10;
    }

    private void sendResult(Player playerA, Player playerB) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", playerA.getUserId().toString());
        data.add("b_id", playerB.getUserId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    private void matchPlayer() { // 尝试进行匹配
        boolean[] used = new boolean[playerList.size()];
        for (int i = 0; i < playerList.size(); i ++) {
            if (used[i]) {
                continue;
            }
            for (int j = i + 1; j < playerList.size(); j ++) {
                if (used[j]) {
                    continue;
                }
                Player playerA = playerList.get(i), playerB = playerList.get(j);
                if (checkMatched(playerA, playerB)) {
                    used[i] = used[j] = true;
                    sendResult(playerA, playerB);
                    break;
                }
            }
        }
        List<Player> newPlayerList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i ++) {
            if (!used[i]) {
                newPlayerList.add(playerList.get(i));
            }
        }
        playerList = newPlayerList;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayer();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
