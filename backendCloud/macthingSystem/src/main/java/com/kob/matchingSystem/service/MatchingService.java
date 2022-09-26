package com.kob.matchingSystem.service;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer botId);
    String removePlayer(Integer userId);
}
