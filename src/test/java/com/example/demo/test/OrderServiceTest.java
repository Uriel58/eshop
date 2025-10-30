package com.example.demo.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testGetOrderById() {
        assertTrue("OrderService 未注入", orderService != null);
        Order order = orderService.getOrderById(1L);
        assertNotNull("Order 不存在", order);
    }
}
