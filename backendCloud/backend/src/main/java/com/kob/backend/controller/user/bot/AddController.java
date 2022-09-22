package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/14
 */
@RestController
@RequestMapping(value = "/user/bot")
public class AddController {
    @Autowired
    private AddService addService;

    @PostMapping("/add")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        return addService.add(data);
    }
}
