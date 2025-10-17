package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 顯示所有商品
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products"; // 對應 templates/products.html
    }

    // 顯示新增表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product"; // 對應 templates/add-product.html
    }

    // 顯示編輯表單
    @GetMapping("/edit/{prodNum}")
    public String showEditForm(@PathVariable("prodNum") Long prodNum, Model model) {
        Product product = productService.getProductById(prodNum);
        model.addAttribute("product", product);
        return "edit-product"; // 對應 templates/edit-product.html
    }

    // 儲存新商品
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // 更新商品
    @PostMapping("/update/{prodNum}")
    public String updateProduct(@PathVariable("prodNum") Long prodNum, @ModelAttribute("product") Product product) {
        productService.updateProduct(prodNum, product);
        return "redirect:/products";
    }

    // 刪除商品
    @GetMapping("/delete/{prodNum}")
    public String deleteProduct(@PathVariable("prodNum") Long prodNum) {
        productService.deleteProduct(prodNum);
        return "redirect:/products";
    }

    // 根據條碼查找商品（非必要，但常見）
    @GetMapping("/barcode/{barcode}")
    public String getProductByBarcode(@PathVariable("barcode") String barcode, Model model) {
        Product product = productService.findByBarcode(barcode);
        model.addAttribute("product", product);
        return "product-detail"; // 或自訂一個單筆詳細頁面
    }

    // 關鍵字模糊查詢
    @GetMapping("/search")
    public String searchByKeyword(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productService.findByKeyword(keyword);
        model.addAttribute("products", products);
        return "products"; // 用同一個列表頁顯示搜尋結果
    }
}
