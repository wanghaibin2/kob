package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/14
 */
@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> resultMap = new HashMap<>();
        if (title == null || title.length() == 0) {
            resultMap.put("error_message", "标题不能为空");
            return resultMap;
        }
        if (title.length() > 30) {
            resultMap.put("error_message", "标题长度不能大于30");
            return resultMap;
        }
        if (description == null || description.length() == 0) {
            description = "这个家伙很懒, 什么也没留下~";
        }
        if (description.length() > 500) {
            resultMap.put("error_message", "Bot描述长度不能大于500");
            return resultMap;
        }

        if (content == null || content.length() == 0) {
            resultMap.put("error_message", "代码不能为空");
            return resultMap;
        }
        if (content.length() > 10000) {
            resultMap.put("error_message", "代码长度不能大于10000");
            return resultMap;
        }

        Bot bot = new Bot(user.getId(), title, description, content);
        botMapper.insert(bot);
        resultMap.put("error_message", "success");
        return resultMap;
    }
}
