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
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderDetailService;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class OrderDetailServiceTest {

    @Autowired
    private OrderDetailService orderDetailService;

    @Test
    public void testGetOrderDetailById() {
        assertTrue("OrderDetailService 未注入", orderDetailService != null);
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(1L); // 使用 getOrderDetailsByOrderId
        assertNotNull("OrderDetail 不存在", orderDetails);
    }
}
