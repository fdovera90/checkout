package com.example.checkoutbackend.service.promotion;

import com.example.checkoutbackend.model.Cart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import com.example.checkoutbackend.model.Promotion;

@Component("TEN_PERCENT_OFF")
public class TenPercentOffStrategy implements PromotionStrategy {
    @Override
    public BigDecimal calculateDiscount(Cart cart, BigDecimal subtotal, Promotion promotion) {
        return subtotal.multiply(new BigDecimal("0.10"));
    }
}
