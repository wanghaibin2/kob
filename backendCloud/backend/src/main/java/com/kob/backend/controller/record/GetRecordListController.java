package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/27
 */
@RestController
@RequestMapping("/record")
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    @GetMapping("/getList")
    public JSONObject getRecordList(@RequestParam Map<String, String> data) {
        Integer pageNumber = Integer.parseInt(data.get("pageNumber"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return getRecordListService.getList(pageNumber, pageSize);
    }
}
