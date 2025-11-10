package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Collections;

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

    private static final int PAGE_SIZE = 8; // 每頁 12 筆

    @GetMapping({ "/", "/home" })
    public String home(HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String prodType,
                       @RequestParam(required = false) String prodLine,
                       @RequestParam(required = false) String description,
                       Model model) {

        // 取得 session 變數
        String name = (String) session.getAttribute("name");
        Long id = (Long) session.getAttribute("id");
        Long customerId = (Long) session.getAttribute("customerId");

        model.addAttribute("name", name);
        model.addAttribute("id", id);

        // 取得 User
        if (id != null) {
            User user = userService.getUserById(id);
            model.addAttribute("users", user);
        }

        // 取得 Cart
        Cart cart = null;
        if (customerId != null) {
            cart = cartService.getCartByCustomerId(customerId);
        }
        model.addAttribute("cart", cart);

        // 取得所有產品類型
        List<String> prodTypes = categoryService.getAllProdTypes();
        model.addAttribute("prodTypes", prodTypes);

        // 取得產品列表（依篩選條件）
        List<Product> allProducts;
        if (prodType != null && !prodType.trim().isEmpty()) {
            allProducts = categoryService.getProductsByFilter(prodType, prodLine, description);
        } else {
            allProducts = productService.getAllProducts();
        }

        // 分頁
        int totalProducts = allProducts.size();
        int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / PAGE_SIZE) : 1;

        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalProducts);
        List<Product> products = (totalProducts > 0 && start < totalProducts) ?
                allProducts.subList(start, end) : Collections.emptyList();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "home"; // 對應 home.html
    }
}
