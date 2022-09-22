package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer userAId, Integer userBId) {
        System.out.println("start game: " + userAId + " " + userBId);
        WebSocketServer.startGame(userAId, userBId);
        return "start game success";
    }
}
