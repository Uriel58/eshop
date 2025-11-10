package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/categories")
public class CategoryController extends LoginBaseController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
    private UserService userService;
	
	@GetMapping
	public String listCategories(HttpSession session,Model model) {
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
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "categories"; // 對應 views/categories.html
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("category", new Category());
		return "add-category"; // 對應 views/add-category.html
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category") Category category) {
		categoryService.saveCategory(category);
		return "redirect:/categories";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Category category = categoryService.getCategoryById(id);
		model.addAttribute("category", category);
		return "edit-category"; // 對應 views/edit-category.html
	}

	@PostMapping("/update")
	public String updateCategory(@ModelAttribute("category") Category category) {
		categoryService.updateCategory(category);
		return "redirect:/categories";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return "redirect:/categories";
	}
}
