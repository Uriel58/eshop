package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController extends LoginBaseController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String listCategories(Model model) {
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

	// 篩選商品
	/*@GetMapping("/getProdLines")
	public String getProdLinesByProdType(@RequestParam("prodType") String prodType, Model model) {
		List<String> prodLines = categoryService.getProdLinesByProdType(prodType);
		model.addAttribute("prodLines", prodLines);
		return "home"; // 返回的可能是局部視圖或其他頁面
	}

	@GetMapping("/getDescriptions")
	public String getDescriptionsByProdTypeAndProdLine(@RequestParam("prodType") String prodType,
			@RequestParam("prodLine") String prodLine, Model model) {
		List<String> descriptions = categoryService.getDescriptionsByProdTypeAndProdLine(prodType, prodLine);
		model.addAttribute("descriptions", descriptions);
		return "home"; // 返回的可能是局部視圖或其他頁面
	}*/
}
