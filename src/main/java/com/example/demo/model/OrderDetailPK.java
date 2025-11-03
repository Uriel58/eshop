package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long order;
    private Long prodNum;

    public OrderDetailPK() {}

    public OrderDetailPK(Long order, Long prodNum) {
    	this.order = order;
        this.prodNum = prodNum;
    }

    public Long getOrder() {
    	return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
    public Long getProdNum() {
        return prodNum;
    }

    public void setProdNum(Long prodNum) {
        this.prodNum = prodNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailPK)) return false;
        OrderDetailPK that = (OrderDetailPK) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(prodNum, that.prodNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, prodNum);
    }
}

