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
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testGetCustomerById() {
        assertTrue("CustomerService 未注入", customerService != null);
        Customer customer = customerService.getCustomerById(1L);
        assertNotNull("Customer 不存在", customer);
        assertEquals("Customer ID 不正確", 1, customer.getCustomerId().longValue());
    }
}
