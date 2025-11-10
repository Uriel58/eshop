package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用來處理產品篩選與分類的 REST API 控制器。
 * 修正重點：
 * 1. 改為 @RestController（等同於 @Controller + @ResponseBody）
 * 2. 每個方法加上 produces="application/json" 明確告訴 Spring 以 JSON 回傳
 * 3. 支援 /api/Select/... 路徑
 */
@RestController
@RequestMapping("/api/Select")
public class SelectProductController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根據產品類型、產品線、描述過濾產品
     */
    @GetMapping(value = "/filter", produces = "application/json")
    public List<Product> filterProducts(
            @RequestParam(required = false) String prodType,
            @RequestParam(required = false) String prodLine,
            @RequestParam(required = false) String description) {
        return categoryService.getProductsByFilter(prodType, prodLine, description);
    }

    /**
     * 取得所有產品類型
     */
    @GetMapping(value = "/getProdTypes", produces = "application/json")
    public List<String> getProdTypes() {
        return categoryService.getAllProdTypes();
    }

    /**
     * 根據產品類型取得產品線
     */
    @GetMapping(value = "/getProdLines", produces = "application/json")
    public List<String> getProdLines(@RequestParam String prodType) {
        return categoryService.getProdLinesByProdType(prodType);
    }

    /**
     * 根據產品類型與產品線取得描述
     */
    @GetMapping(value = "/getDescriptions", produces = "application/json")
    public List<String> getDescriptions(@RequestParam String prodType,
                                        @RequestParam String prodLine) {
        return categoryService.getDescriptionsByProdTypeAndProdLine(prodType, prodLine);
    }
}
