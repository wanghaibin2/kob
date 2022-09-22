package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :王冰冰
 * @date : 2022/9/4
 */
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getAll")
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        userList = userMapper.selectList(null);
        return userList;
    }

    @GetMapping("/getById/{userName}")
    public List<User> getById(@RequestParam(value = "id") Integer id,
                        @PathVariable(value = "userName") String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id", id).le("id", id + 1);
        queryWrapper.eq("user_name", userName);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/add")
    public String addUser(@RequestParam(value = "userName") String userName,
                          @RequestParam(value = "password") String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(encodePassword);
        userMapper.insert(user);
        return "Add Successfully!";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Integer id) {
        userMapper.deleteById(id);
        return "delete Successfully";
    }
}
