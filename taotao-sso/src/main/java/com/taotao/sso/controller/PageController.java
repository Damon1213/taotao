package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hu on 2018-06-13.
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/showRegister")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/showLogin")
    public String showLogin(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }
}
