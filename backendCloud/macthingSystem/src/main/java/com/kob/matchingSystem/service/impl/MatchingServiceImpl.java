package com.kob.matchingSystem.service.impl;

import com.kob.matchingSystem.service.MatchingService;
import com.kob.matchingSystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthOptionPaneUI;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
