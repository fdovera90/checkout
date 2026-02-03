package com.example.checkoutbackend.service.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentDiscountFactory {
    
    public BigDecimal getDiscountPercentage(String paymentMethodType) {
        if ("CREDIT_CARD".equalsIgnoreCase(paymentMethodType)) {
            return new BigDecimal("0.00"); // 0%
        } else if ("CASH".equalsIgnoreCase(paymentMethodType)) {
            return new BigDecimal("0.15"); // 15% discount
        } else if ("DEBIT".equalsIgnoreCase(paymentMethodType)) {
            return new BigDecimal("0.10"); // 10% discount
        }
        return BigDecimal.ZERO;
    }
}
