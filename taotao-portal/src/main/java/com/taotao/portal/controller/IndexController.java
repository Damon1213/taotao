package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hu on 2018-05-28.
 */
@Controller
public class IndexController {
    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }
}
