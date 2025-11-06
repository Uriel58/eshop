package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import java.util.List;

public interface CategoryService {
	void saveCategory(Category category);

	void updateCategory(Category category);

	void deleteCategory(Long id);

	Category getCategoryById(Long id);

	List<Category> getAllCategories();

	// 篩選
	List<String> getAllProdTypes();
    List<String> getProdLinesByProdType(String prodType);
    List<String> getDescriptionsByProdTypeAndProdLine(String prodType, String prodLine);
    List<Product> getProductsByFilter(String prodType, String prodLine, String description);
}
