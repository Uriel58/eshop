package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ShippingConfig {

	@Value("${shipping.pick_up}")
	private int pickUp;

	@Value("${shipping.home_delivery}")
	private int homeDelivery;

	@Value("${shipping.postal}")
	private int postal;

	@Value("${shipping.store_delivery}")
	private int storeDelivery;

	public BigDecimal getPickUp() {
        return BigDecimal.valueOf(pickUp);
    }

    public BigDecimal getHomeDelivery() {
        return BigDecimal.valueOf(homeDelivery);
    }

    public BigDecimal getPostal() {
        return BigDecimal.valueOf(postal);
    }

    public BigDecimal getStoreDelivery() {
        return BigDecimal.valueOf(storeDelivery);
    }
    
}

