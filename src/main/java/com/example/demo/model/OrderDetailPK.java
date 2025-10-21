package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ordNum;
    private Long prodNum;

    public OrderDetailPK() {}

    public OrderDetailPK(Long ordNum, Long prodNum) {
        this.ordNum = ordNum;
        this.prodNum = prodNum;
    }

    public Long getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(Long ordNum) {
        this.ordNum = ordNum;
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
        return Objects.equals(ordNum, that.ordNum) &&
                Objects.equals(prodNum, that.prodNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordNum, prodNum);
    }
}

