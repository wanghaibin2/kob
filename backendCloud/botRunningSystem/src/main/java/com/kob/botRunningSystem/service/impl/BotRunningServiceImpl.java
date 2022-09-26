package com.kob.botRunningSystem.service.impl;

import com.kob.botRunningSystem.service.BotRunningService;
import com.kob.botRunningSystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

/**
 * @author :王冰冰
 * @date : 2022/9/23
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
}
