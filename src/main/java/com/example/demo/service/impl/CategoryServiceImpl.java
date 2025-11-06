package com.example.demo.service.impl;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
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

	// 篩選
	@Override
    public List<String> getAllProdTypes() {
        return categoryDAO.findAllProdTypes();
    }

    @Override
    public List<String> getProdLinesByProdType(String prodType) {
        return categoryDAO.findProdLinesByProdType(prodType);
    }

    @Override
    public List<String> getDescriptionsByProdTypeAndProdLine(String prodType, String prodLine) {
        return categoryDAO.findDescriptionsByProdTypeAndProdLine(prodType, prodLine);
    }
    
    @Override
    public List<Product> getProductsByFilter(String prodType, String prodLine, String description) {
        return categoryDAO.findProductsByCategoryFilter(prodType, prodLine, description);
    }
}
