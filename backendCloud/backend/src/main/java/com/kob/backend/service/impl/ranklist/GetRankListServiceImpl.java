package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :王冰冰
 * @date : 2022/9/28
 */
@Service
public class GetRankListServiceImpl implements GetRankListService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer pageNumber, Integer pageSize) {
        IPage<User> userIPage = new Page<>(pageNumber, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.orderByDesc("rating");
        List<User> userList = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject jsonObject = new JSONObject();
        for (User it : userList) {
            it.setPassword(null);
        }
        jsonObject.put("users", userList);
        jsonObject.put("total_user", userMapper.selectCount(null));
        return jsonObject;
    }
}
