package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author :王冰冰
 * @date : 2022/9/15
 */

@RestController
@RequestMapping("/user/bot")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public List<Bot> search() {
        return searchService.search();
    }
}
