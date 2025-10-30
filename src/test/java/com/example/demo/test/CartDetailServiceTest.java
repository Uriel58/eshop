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
import com.example.demo.model.CartDetail;
import com.example.demo.service.CartDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class CartDetailServiceTest {

    @Autowired
    private CartDetailService cartDetailService;

    @Test
    public void testGetCartDetailById() {
    	assertTrue("CartDetailService 未注入", cartDetailService != null);
        List<CartDetail> cartDetail = cartDetailService.getDetailsByCart(1L);
        assertNotNull("CartDetail 不存在", cartDetail);
    }
}
