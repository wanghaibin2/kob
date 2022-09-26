package com.kob.backend.service.pk;

import org.springframework.stereotype.Service;

/**
 * @author :王冰冰
 * @date : 2022/9/26
 */
public interface ReceiveBotMoveService {
    String receiveBotMove(Integer userId, Integer direction);
}
