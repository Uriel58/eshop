package com.example.demo.service.impl;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;

	@Override
	public void saveCategory(Category category) {
		categoryDAO.save(category);
	}

	@Override
	public void updateCategory(Category category) {
		categoryDAO.update(category);
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = categoryDAO.findById(id);
		if (category != null) {
			categoryDAO.delete(category);
		}
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryDAO.findById(id);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryDAO.findAll();
	}

	// 根據 prodType 查詢 prodLine
	@Override
	public List<String> getProdLinesByProdType(String prodType) {
		return categoryDAO.findProdLinesByProdType(prodType);
	}

	// 實現根據 prodType 和 prodLine 查詢 description
	@Override
	public List<String> getDescriptionsByProdTypeAndProdLine(String prodType, String prodLine) {
		return categoryDAO.findDescriptionsByProdTypeAndProdLine(prodType, prodLine);
	}
}
