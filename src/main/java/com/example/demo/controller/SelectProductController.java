package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Select")
public class SelectProductController {
    
    @Autowired
    private CategoryService categoryService;
    
    private static final int PAGE_SIZE = 8; // 每頁 8 筆

    /**
     * 根據產品類型、產品線、描述過濾產品 (帶分頁)
     */
    @GetMapping(value = "/filter", produces = "application/json")
    @Transactional(readOnly = true)
    public Map<String, Object> filterProducts(
            @RequestParam(required = false) String prodType,
            @RequestParam(required = false) String prodLine,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page) {
        
        // 取得所有符合條件的產品
        List<Product> allProducts = categoryService.getProductsByFilter(prodType, prodLine, description);
        
        // 計算分頁
        int totalProducts = allProducts.size();
        int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / PAGE_SIZE) : 1;
        
        // 確保頁碼合法
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;
        
        // 取得當前頁的產品
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalProducts);
        
        List<Product> pageProducts = (totalProducts > 0 && start < totalProducts) 
            ? allProducts.subList(start, end) 
            : List.of();
        
        // 轉換為 DTO
        List<ProductDTO> productDTOs = pageProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 建立回應物件
        Map<String, Object> response = new HashMap<>();
        response.put("products", productDTOs);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);
        
        return response;
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

    /**
     * 將 Product Entity 轉換為 ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdNum(product.getProdNum());
        dto.setProdName(product.getProdName());
        dto.setProdPrice(product.getProdPrice());
        dto.setProdInfo(product.getProdInfo());
        dto.setProdKeywords(product.getProdKeywords());
        dto.setProdBarcode(product.getProdBarcode());
        dto.setCreatedTime(product.getCreatedTime());
        dto.setProdTags(product.getProdTags());
        dto.setProdImages(product.getProdImages());
        dto.setProdStockQty(product.getProdStockQty());
        
        // 設定 Category 相關資訊
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setProdType(product.getCategory().getProdType());
            dto.setProdLine(product.getCategory().getProdLine());
            dto.setDescription(product.getCategory().getDescription());
        }
        
        return dto;
    }
}