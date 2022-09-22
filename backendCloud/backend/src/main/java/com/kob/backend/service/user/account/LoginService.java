package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/6
 */
public interface LoginService {
    public Map<String, String> getToken(String username, String password);
}
