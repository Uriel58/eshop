package com.example.demo.dao;

import com.example.demo.model.Category;
import java.util.List;

public interface CategoryDAO {
	void save(Category category);

	void update(Category category);

	void delete(Category category);

	Category findById(Long id);

	List<Category> findAll();

	// 根據 prodType 查詢 prodLine
	List<String> findProdLinesByProdType(String prodType);

	// 根據 prodType 和 prodLine 查詢 description
	List<String> findDescriptionsByProdTypeAndProdLine(String prodType, String prodLine);
}
