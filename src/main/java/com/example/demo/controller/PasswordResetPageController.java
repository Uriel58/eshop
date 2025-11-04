package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PasswordResetPageController {

    @GetMapping("/password-reset-page")
    public String passwordResetPage() {
        return "password-reset";  // 對應 WEB-INF/views/password-reset.html
    }
}
