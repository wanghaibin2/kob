package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;

/**
 * @author :王冰冰
 * @date : 2022/9/28
 */
public interface GetRankListService {
    JSONObject getList(Integer pageNumber, Integer pageSize);
}
