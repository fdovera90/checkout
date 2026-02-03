package com.example.checkoutbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResult {
    private BigDecimal subtotal;
    private BigDecimal productDiscount;
    private BigDecimal promotionalDiscount;
    private BigDecimal shippingCost;
    private BigDecimal paymentDiscount;
    private BigDecimal total;
}
