package com.example.demo.service.impl;

import com.example.demo.dao.CartDetailDAO;
import com.example.demo.model.CartDetail;
import com.example.demo.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailDAO cartDetailDao;

    @Override
    public CartDetail getDetail(Long id) {
        return cartDetailDao.findById(id);
    }

    @Override
    public List<CartDetail> getAllDetails() {
        return cartDetailDao.findAll();
    }

    @Override
    public void saveDetail(CartDetail detail) {
        cartDetailDao.save(detail);
    }

    @Override
    public void updateDetail(CartDetail detail) {
        cartDetailDao.update(detail);
    }

    @Override
    public void deleteDetail(Long id) {
        cartDetailDao.delete(id);
    }

    @Override
    public List<CartDetail> getDetailsByCart(Long cartId) {
        return cartDetailDao.findByCartId(cartId);
    }
}

