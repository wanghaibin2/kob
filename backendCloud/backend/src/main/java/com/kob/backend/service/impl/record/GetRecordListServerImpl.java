package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :王冰冰
 * @date : 2022/9/27
 */
@Service
public class GetRecordListServerImpl implements GetRecordListService {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    // JSONObject 返回的是map, 即数据对的形式
    @Override
    public JSONObject getList(Integer pageNumber, Integer pageSize) {
        // 默认展示10条记录
        IPage<Record> recordIPage = new Page<>(pageNumber, pageSize);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");  //展示最新的记录
        List<Record> recordList = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if (!recordList.isEmpty()) {
            for (Record record : recordList) {
                User userA = userMapper.selectById(record.getAId());
                User userB = userMapper.selectById(record.getBId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("a_photo", userA.getPhoto());
                jsonObject.put("a_username", userA.getUserName());
                jsonObject.put("b_photo", userB.getPhoto());
                jsonObject.put("b_username", userB.getUserName());
                String result = "平局";
                if (Game.LOSER_A.equals(record.getLoser())) {
                    result = "玩家B胜利";
                } else if (Game.LOSER_B.equals(record.getLoser())) {
                    result = "玩家A胜利";
                }
                jsonObject.put("result", result);
                jsonObject.put("record", record);
                jsonObjectList.add(jsonObject);
            }
        }
        resp.put("records", jsonObjectList);
        resp.put("total_record", recordMapper.selectCount(null));
        return resp;
    }
}
