package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @author :王冰冰
 * @date : 2022/9/27
 */
public interface GetRecordListService {
    JSONObject getList(Integer pageNumber, Integer pageSize);
}
