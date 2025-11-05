package com.example.demo.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testSaveCategory() {
        // 创建一个新的 Category 对象
        Category category = new Category("电子产品", "智能手机", "所有智能手机产品的分类");
        
        // 使用服务保存分类
        categoryService.saveCategory(category);
        
        // 检查分类是否已保存（ID 应该由持久化层设置）
        assertNotNull("保存后，分类的 ID 不应为 null", category.getId());
    }

    @Test
    public void testUpdateCategory() {
        // 创建一个新的 Category 对象
        Category category = new Category("电子产品", "智能手机", "所有智能手机产品的分类");
        
        // 保存该分类
        categoryService.saveCategory(category);
        
        // 更新分类的描述
        category.setDescription("更新后的智能手机分类描述");
        
        // 使用服务更新分类
        categoryService.updateCategory(category);
        
        // 获取更新后的分类并确认描述已被更新
        Category updatedCategory = categoryService.getCategoryById(category.getId());
        assertTrue("分类描述应该已更新", "更新后的智能手机分类描述".equals(updatedCategory.getDescription()));
    }

    @Test
    public void testDeleteCategory() {
        // 创建一个新的 Category 对象
        Category category = new Category("电子产品", "智能手机", "所有智能手机产品的分类");
        
        // 保存该分类
        categoryService.saveCategory(category);
        
        // 获取保存的分类 ID
        Long categoryId = category.getId();
        
        // 使用服务删除该分类
        categoryService.deleteCategory(categoryId);
        
        // 尝试重新获取该分类，应该返回 null，因为它已被删除
        Category deletedCategory = categoryService.getCategoryById(categoryId);
        assertTrue("分类应该已经被删除", deletedCategory == null);
    }

    @Test
    public void testGetAllCategories() {
        // 获取所有分类
        List<Category> categories = categoryService.getAllCategories();
        
        // 确保返回的分类列表不为空
        assertTrue("分类列表不应为空", !categories.isEmpty());
    }

    @Test
    public void testGetCategoryById() {
        // 创建一个新的 Category 对象
        Category category = new Category("电子产品", "智能手机", "所有智能手机产品的分类");
        
        // 保存该分类
        categoryService.saveCategory(category);
        
        // 根据 ID 获取该分类
        Category fetchedCategory = categoryService.getCategoryById(category.getId());
        
        // 确保获取到的分类不为空，并且与保存的分类匹配
        assertNotNull("获取的分类不应为 null", fetchedCategory);
        assertTrue("获取的分类 ID 应该与保存的分类 ID 一致", fetchedCategory.getId().equals(category.getId()));
    }
}
