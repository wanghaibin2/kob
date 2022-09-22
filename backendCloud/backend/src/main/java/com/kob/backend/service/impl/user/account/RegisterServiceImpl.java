package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author :王冰冰
 * @date : 2022/9/7
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    private final static String errorMessage = "error_message";
    private final static String successStr = "success";
    private final static String passwordStr = "密码不能为空";
    private final static String usernameStr = "用户名不能为空";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null || username.length() == 0) {
            map.put(errorMessage, usernameStr);
            return map;
        }
        if (password == null || confirmedPassword == null || password.length() == 0 || confirmedPassword.length() == 0) {
            map.put(errorMessage, passwordStr);
            return map;
        }
        username = username.trim(); // 删掉首尾的空白字符
        if (username.length() == 0) {
            map.put(errorMessage, usernameStr);
            return map;
        }
        if (username.length() > 100) {
            map.put(errorMessage, "用户名长度不能大于100");
            return map;
        }
        if (password.length() == 100 || confirmedPassword.length() == 100) {
            map.put(errorMessage, passwordStr);
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put(errorMessage, "密码长度不能大于100");
            return map;
        }
        if (!Objects.equals(password, confirmedPassword)) {
            map.put(errorMessage, "两次输入的密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        List<User> userList = userMapper.selectList(queryWrapper);
        if (!userList.isEmpty()) {
            map.put(errorMessage, "用户名已存在");
            return map;
        }
        // 密码加密
        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/52953_lg_2c3ff42a05.jpg";
        User user = new User(null, username, encodePassword, photo, 1500);
        userMapper.insert(user);
        map.put(errorMessage, successStr);
        return map;
    }
}
