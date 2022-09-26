package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author :王冰冰
 * @date : 2022/9/22
 */
@RestController
@RequestMapping("/pk")
public class StartGameController {
    @Autowired
    private StartGameService startGameService;

    @PostMapping("/start/game")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer playAId = Integer.valueOf(Objects.requireNonNull(data.getFirst("a_id")));
        Integer playABotId = Integer.valueOf(Objects.requireNonNull(data.getFirst("a_bot_id")));
        Integer playBId = Integer.valueOf(Objects.requireNonNull(data.getFirst("b_id")));
        Integer playBBotId = Integer.valueOf(Objects.requireNonNull(data.getFirst("b_bot_id")));
        return startGameService.startGame(playAId, playBId, playABotId, playBBotId);
    }
}
