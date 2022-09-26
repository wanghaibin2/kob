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
    public String startGame(Integer userAId, Integer userBId, Integer userABotId, Integer userBBotId) {
        WebSocketServer.startGame(userAId, userBId, userABotId, userBBotId);
        return "start game success";
    }
}
