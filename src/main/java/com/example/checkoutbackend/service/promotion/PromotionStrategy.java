package com.example.checkoutbackend.service.promotion;

import com.example.checkoutbackend.model.Cart;
import java.math.BigDecimal;

import com.example.checkoutbackend.model.Promotion;

public interface PromotionStrategy {
    BigDecimal calculateDiscount(Cart cart, BigDecimal subtotal, Promotion promotion);
}
