package com.concentrate.gpt.gptflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2024/3/3.
 */
@Controller
public class HomeController {

    private static final String HOME_PAGE = "home";

    @RequestMapping("home")
    public String home(){
        return HOME_PAGE;
    }

}
