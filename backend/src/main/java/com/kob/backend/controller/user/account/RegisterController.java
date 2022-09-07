package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/7
 */
@RestController
@RequestMapping("/user/account")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> userMap) {
        Map<String, String> map = new HashMap<>();
        String username = userMap.get("username");
        String password = userMap.get("password");
        String confirmedPassword = userMap.get("confirmedPassword");
        map = registerService.register(username, password, confirmedPassword);
        return map;
    }
}
