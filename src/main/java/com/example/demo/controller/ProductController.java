package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController extends LoginBaseController{

    @Autowired
    private ProductService productService;

    // 顯示所有商品
    @GetMapping
    public String listProducts(HttpSession session,Model model) {
    	Long id = (Long) session.getAttribute("id");
        String name = (String) session.getAttribute("name");

        // 如果沒登入，導回登入頁
        /*if (id == null) {
            return "redirect:/login";
        }*/

        model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("products", productService.getAllProducts());
        return "products";// 對應 templates/products.html
    }

    // 回首頁（session 顯示）
    @GetMapping("/products")
    public String home(HttpSession session, Model model) {
        String name = (String) session.getAttribute("name");
        Long id = (Long) session.getAttribute("id");
        
        model.addAttribute("name", name);
        model.addAttribute("id", id);
        
       
        return "home";
    }
    
    

    // 顯示新增表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product"; // 對應 templates/add-product.html
    }

    // 顯示編輯表單
    /*@GetMapping("/edit/{prodNum}")
    public String showEditForm(@PathVariable("prodNum") Long prodNum, Model model) {
        Product product = productService.getProductById(prodNum);
        model.addAttribute("product", product);
        return "edit-product"; // 對應 templates/edit-product.html
    }*/
    @GetMapping("/edit/{prodNum}")
    public String showEditForm(@PathVariable("prodNum") Long prodNum, Model model) {
        Product product = productService.getProductById(prodNum);
        if (product.getCreatedTime() != null) {
            product.setCreatedTime(product.getCreatedTime().withZoneSameInstant(ZoneId.of("Asia/Taipei")));
        }
        model.addAttribute("product", product);
        return "edit-product";
    }


    // 儲存新商品
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // ✅ 更新商品（已加入 LocalDateTime → ZonedDateTime 轉換）
    /*@PostMapping("/update/{prodNum}")
    public String updateProduct(
            @PathVariable("prodNum") Long prodNum,
            @ModelAttribute Product product,
            @RequestParam("createdTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime createdTime) {

        // 將 LocalDateTime 轉為台北時區的 ZonedDateTime
        product.setCreatedTime(createdTime.atZone(ZoneId.of("Asia/Taipei")));

        productService.updateProduct(prodNum, product);
        return "redirect:/products";
    }*/
    @PostMapping("/update/{prodNum}")
    public String updateProduct(
        @PathVariable("prodNum") Long prodNum,
        @ModelAttribute Product product) {

        // 不需要從前端取 createdTime，保持原來的值
        Product existing = productService.getProductById(prodNum);
        if (existing != null) {
            product.setCreatedTime(existing.getCreatedTime());
        }

        productService.updateProduct(prodNum, product);
        return "redirect:/products";
    }


    // 刪除商品
    @GetMapping("/delete/{prodNum}")
    public String deleteProduct(@PathVariable("prodNum") Long prodNum) {
        productService.deleteProduct(prodNum);
        return "redirect:/products";
    }

    // 根據條碼查找商品
    @GetMapping("/barcode/{barcode}")
    public String getProductByBarcode(@PathVariable("barcode") String barcode, Model model) {
        Product product = productService.findByBarcode(barcode);
        model.addAttribute("product", product);
        return "/";
    }

    // 關鍵字模糊查詢
    @GetMapping("/search")
    public String searchByKeyword(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productService.findByKeyword(keyword);
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/details/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
    	Product product = productService.getProductByIdWithCategory(id);
        model.addAttribute("product", product);

        // 直接從 session 取得登入者，繼承 BaseController，無需重複編寫
        if (model.containsAttribute("customer")) {
            model.addAttribute("successMessage", "商品已成功加入購物車！");
        }
        return "products_details"; // 指向 product_details.html
    }
}
