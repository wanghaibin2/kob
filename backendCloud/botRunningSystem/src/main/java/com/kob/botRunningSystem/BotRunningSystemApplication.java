package com.kob.botRunningSystem;

import com.kob.botRunningSystem.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :王冰冰
 * @date : ${DATE}
 */
@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.botPool.start();
        SpringApplication.run(BotRunningSystemApplication.class, args);
    }
}