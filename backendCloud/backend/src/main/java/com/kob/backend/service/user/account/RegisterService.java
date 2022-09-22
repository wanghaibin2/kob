package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/6
 */
public interface RegisterService {
    public Map<String, String> register(String username, String password, String confirmedPassword);
}
