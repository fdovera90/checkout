package com.example.checkoutbackend.service.promotion;

import com.example.checkoutbackend.model.Cart;
import com.example.checkoutbackend.model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import com.example.checkoutbackend.model.Promotion;

@Component("PRODUCT_SPECIFIC")
public class ProductSpecificStrategy implements PromotionStrategy {
    @Override
    public BigDecimal calculateDiscount(Cart cart, BigDecimal subtotal, Promotion promotion) {
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal discountPerUnit = promotion.getConfigValue(); // Configured in DB
        
        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                if ("p-001".equals(item.getSku())) { // Laptop target still hardcoded for now
                    discount = discount.add(discountPerUnit.multiply(new BigDecimal(item.getQuantity())));
                }
            }
        }
        return discount;
    }
}
