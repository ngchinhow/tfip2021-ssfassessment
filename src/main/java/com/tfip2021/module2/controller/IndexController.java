package com.tfip2021.module2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = { "/", "/index.html" })
public class IndexController {
    @GetMapping(produces = { "text/html" })
    public String indexPage() {
        return "index";
    }
}
