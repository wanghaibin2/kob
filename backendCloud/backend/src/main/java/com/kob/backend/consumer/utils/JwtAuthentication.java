package com.kob.backend.consumer.utils;

import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @author :王冰冰
 * @date : 2022/9/18
 */
public class JwtAuthentication {
    public static Integer getUserId(String token) {
        Integer userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
