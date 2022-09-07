package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/6
 */
@RequestMapping("/user/account")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> userMap) {
        String username = userMap.get("username");
        String password = userMap.get("password");
        return loginService.getToken(username, password);
    }
}
