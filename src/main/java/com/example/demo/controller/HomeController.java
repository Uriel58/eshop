package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"id", "name"})
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
    	String name = (String) session.getAttribute("name");
        Long id = (Long) session.getAttribute("id");

        /*if (name == null || id == null) {
        	
            return "redirect:/login"; // 讀取到尚未登入，強制導回 login 頁
        }*/

        model.addAttribute("name", name);
        model.addAttribute("id", id);
        return "home";
    }
}
