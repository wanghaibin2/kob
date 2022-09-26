package com.kob.botRunningSystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :王冰冰
 * @date : 2022/9/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private Integer userId;
    private String  botCode;
    private String input;
}
