package com.kob.backend.service.pk;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
public interface StartGameService {
    String startGame(Integer userAId, Integer userBId, Integer userABotId, Integer userBBotId);
}
