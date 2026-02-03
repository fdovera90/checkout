package com.example.checkoutbackend.service;

import com.example.checkoutbackend.model.*;
import com.example.checkoutbackend.repository.PaymentMethodRepository;
import com.example.checkoutbackend.repository.ProductRepository;
import com.example.checkoutbackend.repository.PromotionRepository;
import com.example.checkoutbackend.service.promotion.PromotionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingService shippingService;
    private final Map<String, PromotionStrategy> promotionStrategies; // Injected by Spring

    private static final BigDecimal PERCENTAGE_DIVISOR = new BigDecimal("100");
    private static final int DECIMAL_SCALE = 0;

    public CheckoutResult processCheckout(List<CartItem> items, ShippingAddress shippingAddress, String paymentMethodType) {
        log.debug("Processing checkout for {} items with payment method: {}", (items != null ? items.size() : 0), paymentMethodType);
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal productListDiscount = BigDecimal.ZERO;

        if (items != null) {
            for (CartItem item : items) {
                Product product = productRepository.findBySku(item.getSku())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getSku()));
                
                BigDecimal qty = new BigDecimal(item.getQuantity());
                subtotal = subtotal.add(product.getPrice().multiply(qty));
                
                if (product.getDiscount() != null && product.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                     BigDecimal discountAmount = calculatePercentageDiscount(product.getPrice(), product.getDiscount());
                     productListDiscount = productListDiscount.add(discountAmount.multiply(qty));
                }
            }
        }
        log.debug("Subtotal: {}, Product list discount: {}", subtotal, productListDiscount);
        
        BigDecimal additionalPromotionDiscount = BigDecimal.ZERO;

        Cart cart = new Cart();
        cart.setItems(items);

        List<Promotion> activePromotions = promotionRepository.findAll();
        for (Promotion promo : activePromotions) {
            if (!"S".equalsIgnoreCase(promo.getActive())) {
                continue;
            }
            PromotionStrategy strategy = promotionStrategies.get(promo.getStrategyType());
            if (strategy != null) {
                BigDecimal discount = strategy.calculateDiscount(cart, subtotal, promo)
                        .setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
                if (discount.compareTo(BigDecimal.ZERO) > 0) {
                    additionalPromotionDiscount = additionalPromotionDiscount.add(discount);
                }
            }
        }
        log.debug("Additional promotion discount: {}", additionalPromotionDiscount);

        BigDecimal shippingCost = shippingService.calculateCost(shippingAddress != null ? shippingAddress.getZoneId() : null);
        log.debug("Shipping cost: {}", shippingCost);

        BigDecimal totalPromotionDiscount = productListDiscount.add(additionalPromotionDiscount);
        BigDecimal intermediateTotal = subtotal.subtract(totalPromotionDiscount).add(shippingCost).max(BigDecimal.ZERO);
        
        BigDecimal paymentDiscountPercent = BigDecimal.ZERO;
        if (paymentMethodType != null) {
            try {
                PaymentMethodType typeEnum = PaymentMethodType.valueOf(paymentMethodType);
                paymentDiscountPercent = paymentMethodRepository.findByType(typeEnum)
                        .map(PaymentMethod::getDiscount)
                        .orElse(BigDecimal.ZERO);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid payment method: " + paymentMethodType);
            }
        }

        BigDecimal paymentDiscount = intermediateTotal.multiply(paymentDiscountPercent).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
        BigDecimal finalTotal = intermediateTotal.subtract(paymentDiscount).max(BigDecimal.ZERO);

        log.info("Process finished. Total discount: {}. Final Total: {}", 
                 totalPromotionDiscount.add(paymentDiscount), finalTotal);

        return new CheckoutResult(subtotal, productListDiscount, additionalPromotionDiscount, shippingCost, paymentDiscount, finalTotal);
    }

    private BigDecimal calculatePercentageDiscount(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage).divide(PERCENTAGE_DIVISOR, DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}
