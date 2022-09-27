package com.kob.backend.controller.racklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author :王冰冰
 * @date : 2022/9/28
 */
@RestController
@RequestMapping("/rank")
public class GetRankListController {
    @Autowired
    private GetRankListService getRankListService;

    @GetMapping("/getList")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer pageNumber = Integer.parseInt(data.get("pageNumber"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return getRankListService.getList(pageNumber, pageSize);
    }
}
