package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Select")
public class SelectProductController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根據產品類型、產品線、描述過濾產品
     */
    @GetMapping(value = "/filter", produces = "application/json")
    @Transactional(readOnly = true)
    public List<ProductDTO> filterProducts(
            @RequestParam(required = false) String prodType,
            @RequestParam(required = false) String prodLine,
            @RequestParam(required = false) String description) {
        
        List<Product> products = categoryService.getProductsByFilter(prodType, prodLine, description);
        
        // 轉換為 DTO
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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