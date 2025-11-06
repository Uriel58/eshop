package com.example.demo.controller;

import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/select-product")
public class SelectProductController {
	@Autowired
    private CategoryService categoryService;

    // 顯示首頁，只載入 prodType
    @GetMapping("/home")
    public String home(Model model) {
        // 一開始只查出所有 prodType
        List<String> prodTypes = categoryService.getAllProdTypes();
        model.addAttribute("prodTypes", prodTypes);
        return "home"; // 導向 home.html / home.jsp
    }

    // AJAX：根據 prodType 查 prodLine
    @GetMapping("/getProdLines")
    @ResponseBody
    public List<String> getProdLines(@RequestParam("prodType") String prodType) {
        return categoryService.getProdLinesByProdType(prodType);
    }

    // AJAX：根據 prodType + prodLine 查 description
    @GetMapping("/getDescriptions")
    @ResponseBody
    public List<String> getDescriptions(
            @RequestParam("prodType") String prodType,
            @RequestParam("prodLine") String prodLine) {
        return categoryService.getDescriptionsByProdTypeAndProdLine(prodType, prodLine);
    }
}
