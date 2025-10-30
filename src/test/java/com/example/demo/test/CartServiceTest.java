package com.example.demo.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Cart;
import com.example.demo.model.Customer;  // 假設 Customer 類存在
import com.example.demo.service.CartService;
import org.springframework.transaction.annotation.Transactional;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    
    
    @Before
    public void setUp() {
        // 在這裡插入測試數據，確保 ID 1L 的 Cart 存在
        Customer customer = new Customer();  // 假設 Customer 有一個無參數構造方法
        customer.setCustomerId(1L);  // 設置顧客 ID

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);  // 使用 Customer 對象設置
        // 假設你有必要的設置其他屬性
        cartService.saveCart(cart); // 保存到資料庫中
    }

    @Test
    public void testGetCartById() {
        assertTrue("CartService 未注入", cartService != null);
        Cart cart = cartService.getCart(1L);  // 根據 Cart ID 查找
        assertNotNull("Cart 不存在", cart);
    }
}
