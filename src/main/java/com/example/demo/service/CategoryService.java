package com.example.demo.service;

import com.example.demo.model.Category;
import java.util.List;

public interface CategoryService {
	void saveCategory(Category category);

	void updateCategory(Category category);

	void deleteCategory(Long id);

	Category getCategoryById(Long id);

	List<Category> getAllCategories();

	// 根據 prodType 查詢 prodLine
	List<String> getProdLinesByProdType(String prodType);

	// 根據 prodType 和 prodLine 查詢 description
	List<String> getDescriptionsByProdTypeAndProdLine(String prodType, String prodLine);

}
