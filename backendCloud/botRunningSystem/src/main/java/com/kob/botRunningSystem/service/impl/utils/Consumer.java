package com.kob.botRunningSystem.service.impl.utils;

import com.kob.botRunningSystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author :王冰冰
 * @date : 2022/9/26
 */
@Component
public class Consumer extends Thread{
    private Bot bot;

    private static RestTemplate restTemplate = new RestTemplate();
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move";

    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeOut(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout); // 最多等待的时间 (s)
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 终端当前线程
        }
    }

    private String addUid(String code, String uid) { // 在code中的Bot类名加上uid
        int k = code.indexOf(" implements com.kob.botRunningSystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        BotInterface botInterface = Reflect.compile("com.kob.botRunningSystem.utils.Bot" + uid,
            addUid(bot.getBotCode(), uid)).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());
        // System.out.println(bot.getUserId() + " " + direction);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
