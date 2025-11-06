package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.CartService;

import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController extends LoginBaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	private static final int PAGE_SIZE = 12; // 每頁10筆

	@GetMapping({ "/", "/home", "/select-product/home" })
	public String home(HttpSession session, @RequestParam(defaultValue = "0") int page, Model model) {

		String name = (String) session.getAttribute("name");
		Long id = (Long) session.getAttribute("id");
		Long customerId = (Long) session.getAttribute("customerId"); // ✅ 從 session 取得 customerId

		model.addAttribute("name", name);
		model.addAttribute("id", id);

		// 取得 User 物件並放進 model，用來偵測user身份
		if (id != null) {
			User user = userService.getUserById(id);
			model.addAttribute("users", user);
		}
		// 取得當前用戶的購物車
		Cart cart = null;
		if (customerId != null) {
			cart = cartService.getCartByCustomerId(customerId); // ✅ 取得 Cart
		}
		model.addAttribute("cart", cart); // 注意這裡傳一個 Cart 對象

		// --- 載入產品類型 ---
		List<String> prodTypes = categoryService.getAllProdTypes();
		model.addAttribute("prodTypes", prodTypes);
		// 先載入所有產品，前端可用 Ajax 過濾
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

	// --- AJAX API 保留 ---
	// --- AJAX API: 取得產品線 ---
	@GetMapping("/select-product/getProdLines")
	@ResponseBody
	public List<String> getProdLines(@RequestParam("prodType") String prodType) {
		return categoryService.getProdLinesByProdType(prodType);
	}

	// --- AJAX API: 取得描述 ---
	@GetMapping("/select-product/getDescriptions")
	@ResponseBody
	public List<String> getDescriptions(@RequestParam("prodType") String prodType,
			@RequestParam("prodLine") String prodLine) {
		return categoryService.getDescriptionsByProdTypeAndProdLine(prodType, prodLine);
	}

	// --- AJAX API: 篩選產品 ---
	@GetMapping("/products/filter")
	@ResponseBody
	public List<Product> filterProducts(@RequestParam(required = false) String prodType,
			@RequestParam(required = false) String prodLine, @RequestParam(required = false) String description) {
		return categoryService.getProductsByFilter(prodType, prodLine, description);
	}
}
