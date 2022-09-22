package com.kob.matchingSystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer waitingTime; // 等待时间
}
