package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @Autor: 王冰冰
 * @Date: 2022/7/16 22:51
 *
 */
@RestController
@RequestMapping("/Pk")
public class BotInfoController {

    @RequestMapping("/getBotInfo")
    public List<String> getBotInfo(){
        List<String> list = new ArrayList<>();
        list.add("WangBb");
        list.add("tiger");
        list.add("apple");
        return list;
    }

    @RequestMapping("/getDictionary")
    public Map<String, String> getDictionary() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "wangbingbing");
        map.put("rating", "520");
        return map;
    }
}
