package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Select")
public class SelectProductController {
	@Autowired
    private CategoryService categoryService;

    @GetMapping("/filter")
    @ResponseBody
    public List<Product> filterProducts(@RequestParam(required = false) String prodType,
                                        @RequestParam(required = false) String prodLine,
                                        @RequestParam(required = false) String description) {
        return categoryService.getProductsByFilter(prodType, prodLine, description);
    }

    @GetMapping("/getProdTypes")
    @ResponseBody
    public List<String> getProdTypes() {
        return categoryService.getAllProdTypes();
    }

    @GetMapping("/getProdLines")
    @ResponseBody
    public List<String> getProdLines(@RequestParam String prodType) {
        return categoryService.getProdLinesByProdType(prodType);
    }

    @GetMapping("/getDescriptions")
    @ResponseBody
    public List<String> getDescriptions(@RequestParam String prodType,
                                        @RequestParam String prodLine) {
        return categoryService.getDescriptionsByProdTypeAndProdLine(prodType, prodLine);
    }
}
