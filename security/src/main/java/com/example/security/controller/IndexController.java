package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("index")
    public String index(){
        return "login";
    }

    @GetMapping("findAll")
    @ResponseBody
    public String findAll(){
        return "findAll";
    }
}
