package com.example.demo.dao;

import com.example.demo.model.Category;
import java.util.List;

public interface CategoryDAO {
	void save(Category category);

	void update(Category category);

	void delete(Category category);

	Category findById(Long id);

	List<Category> findAll();

	// 篩選
	List<String> findAllProdTypes();
    List<String> findProdLinesByProdType(String prodType);
    List<String> findDescriptionsByProdTypeAndProdLine(String prodType, String prodLine);
}
