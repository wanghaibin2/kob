package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * @Autor: 王冰冰
 * @Date: 2022/7/16 22:44
 *
 */
@Controller
@RequestMapping("/pk")
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "pk/index.html";
    }
}
