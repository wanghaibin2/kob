package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author :王冰冰
 * @date : 2022/9/15
 */
@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        Map<String, String> resultMap = new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        int botId = Integer.parseInt(data.get("botId"));
        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            resultMap.put("error_message", "bot不存在");
            return resultMap;
        }
        if (!Objects.equals(bot.getUserId(), user.getId())) {
            resultMap.put("error_message", "没有权限修改");
            return resultMap;
        }
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("context");
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
        Bot newBot = new Bot(bot.getId(), user.getId(), title, description, content);
        botMapper.updateById(newBot);
        resultMap.put("error_message", "success");
        return resultMap;
    }
}
