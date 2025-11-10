package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.User;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/admin")
public class AdminController extends LoginBaseController {
	
	@Autowired
    private UserService userService;
	
    @GetMapping
    public String adminHome(HttpSession session, Model model){
    	// 檢查是否登入
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // 檢查是否為 customer (customer 不能進入)
        User user = userService.getUserById(userId);
        if (user == null || "customer".equals(user.getIdentifyName())) {
            return "redirect:/"; // 導向首頁或顯示無權限頁面
        }
        // 指向 templates/admin.html
        return "admin_controller";
    }
}