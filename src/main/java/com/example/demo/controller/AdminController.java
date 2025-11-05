package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController extends LoginBaseController {

    @GetMapping
    public String adminHome() {
        // 指向 templates/admin.html
        return "admin_controller";
    }
}