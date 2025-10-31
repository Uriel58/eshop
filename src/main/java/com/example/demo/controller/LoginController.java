package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.http.HttpSession;
@Controller
@SessionAttributes({"id", "name"}) // 宣告要存入 Session 的屬性
public class LoginController{

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /*@PostMapping("/login")
    public String login(@ModelAttribute("user") User formUser, Model model) {
        User user = userService.findByEmail(formUser.getEmail());
        if (user != null && user.getPassword().equals(formUser.getPassword())) {
            // 將 id 和 name 加入 Model，@SessionAttributes 會自動存到 Session
            model.addAttribute("name", user.getName());
            model.addAttribute("id", user.getId());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    	
    }*/
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User formUser, HttpSession session, Model model) {
        User user = userService.findByEmail(formUser.getEmail());
        if (user != null && user.getPassword().equals(formUser.getPassword())) {
            session.setAttribute("id", user.getId());
            session.setAttribute("name", user.getName());
            if(user.getCustomer() != null) {
                session.setAttribute("customerId", user.getCustomer().getCustomerId());
            }
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除整個 Session
        return "redirect:/login";
    }
}
