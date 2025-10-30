package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController extends LoginBaseController{

	@Autowired
	private ProductService productService;

	private static final int PAGE_SIZE = 12; // 每頁10筆

	@GetMapping("/")
	public String home(HttpSession session, @RequestParam(defaultValue = "0") int page, Model model) {

		String name = (String) session.getAttribute("name");
		Long id = (Long) session.getAttribute("id");

		model.addAttribute("name", name);
		model.addAttribute("id", id);

		List<Product> allProducts = productService.getAllProducts();

		int totalProducts = allProducts.size();
		int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

		// 範圍界限檢查
		if (page < 0)
			page = 0;
		if (page >= totalPages)
			page = totalPages - 1;

		int start = page * PAGE_SIZE;
		int end = Math.min(start + PAGE_SIZE, totalProducts);
		// 確保 start 和 end 索引不會超出列表範圍
		if (start < 0)
			start = 0;
		if (end > totalProducts)
			end = totalProducts;

		List<Product> products = allProducts.subList(start, end);

		model.addAttribute("products", products);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);

		return "home"; // 對應 home.html
	}
}
