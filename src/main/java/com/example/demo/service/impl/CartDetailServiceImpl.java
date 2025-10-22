package com.example.demo.service.impl;

import com.example.demo.dao.CartDetailDAO;
import com.example.demo.model.CartDetail;
import com.example.demo.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailDAO cartDetailDAO;

    @Override
    public CartDetail findById(Long id) {
        return cartDetailDAO.findById(id);
    }

    @Override
    public List<CartDetail> getCartDetailsByCustomerId(Long customerId) {
        return cartDetailDAO.findByCustomerId(customerId);
    }

    @Override
    public void saveCartDetail(CartDetail cartDetail) {
        cartDetailDAO.save(cartDetail);
    }

    @Override
    public void updateCartDetail(CartDetail cartDetail) {
        cartDetailDAO.update(cartDetail);
    }

    @Override
    public void deleteCartDetail(Long id) {
        cartDetailDAO.deleteById(id);
    }

    @Override
    public void deleteAllByCustomerId(Long customerId) {
        cartDetailDAO.deleteAllByCustomerId(customerId);
    }

    @Override
    public BigDecimal calculateCartTotal(Long customerId) {
        List<CartDetail> cartDetails = cartDetailDAO.findByCustomerId(customerId);
        return cartDetails.stream()
                .map(cd -> cd.getProdPrice()
                        .multiply(BigDecimal.valueOf(cd.getCartQty()))
                        .add(cd.getShippingFee() != null ? cd.getShippingFee() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
