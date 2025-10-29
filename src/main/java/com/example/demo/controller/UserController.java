package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@SessionAttributes({"id", "name"}) // 宣告要存入 Session 的屬性
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user"; // 對應 /WEB-INF/views/edit-user.html
    }
    
    @PostMapping("/update/{id}")//HTML <form> 只支持 GET 和 POST，将后端改为接受 POST（简单、兼容 HTML）
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
   
        userService.updateUser(id, user);
        return "redirect:/users";
    }
    
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
    	
        userService.saveUser(user);
        return "redirect:/users";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
    
}