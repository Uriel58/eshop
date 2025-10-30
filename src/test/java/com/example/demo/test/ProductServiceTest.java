package com.example.demo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testGetProductById() {
        assertTrue("ProductService 未注入", productService != null);
        Product product = productService.getProductById(1L);
        assertNotNull("Product 不存在", product);
        assertEquals("Product ID 不正確", 1, product.getProdNum().longValue());
    }
}
