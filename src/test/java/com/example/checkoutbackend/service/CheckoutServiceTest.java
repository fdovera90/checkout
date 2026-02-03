package com.example.checkoutbackend.service;

import com.example.checkoutbackend.model.*;
import com.example.checkoutbackend.repository.PaymentMethodRepository;
import com.example.checkoutbackend.repository.ProductRepository;
import com.example.checkoutbackend.repository.PromotionRepository;
import com.example.checkoutbackend.service.promotion.PromotionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private Map<String, PromotionStrategy> promotionStrategies;

    @Mock
    private ShippingService shippingService;

    @Mock
    private PromotionStrategy promoStrategy;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCheckoutWithPromotionsAndPaymentDiscount() {
        Product p1 = new Product(1L, "p-001", "Laptop", new BigDecimal("1000.00"), new BigDecimal("20.00"), "Tech"); 
        when(productRepository.findBySku("p-001")).thenReturn(Optional.of(p1));

        CartItem item = new CartItem();
        item.setSku("p-001");
        item.setQuantity(1);
        List<CartItem> items = List.of(item);

        Promotion promo = new Promotion(1L, "Summer Sale", "TEN_PERCENT_OFF", new BigDecimal("0.00"), "S"); 
        when(promotionRepository.findAll()).thenReturn(List.of(promo));
        when(promotionStrategies.get("TEN_PERCENT_OFF")).thenReturn(promoStrategy);
        when(promoStrategy.calculateDiscount(any(), any(), any())).thenReturn(new BigDecimal("100.00")); 
        
        when(shippingService.calculateCost(any())).thenReturn(new BigDecimal("10.00"));

        PaymentMethod pm = new PaymentMethod(1L, "Cash", PaymentMethodType.CASH, new BigDecimal("15.00"));
        when(paymentMethodRepository.findByType(PaymentMethodType.CASH)).thenReturn(Optional.of(pm));

        CheckoutResult result = checkoutService.processCheckout(items, null, "CASH");

        assertEquals(0, new BigDecimal("1000").compareTo(result.getSubtotal()));
        assertEquals(0, new BigDecimal("200").compareTo(result.getProductDiscount()));
        assertEquals(0, new BigDecimal("100").compareTo(result.getPromotionalDiscount()));
        assertEquals(0, new BigDecimal("10").compareTo(result.getShippingCost()));
        assertEquals(0, new BigDecimal("107").compareTo(result.getPaymentDiscount()));
        assertEquals(0, new BigDecimal("603").compareTo(result.getTotal()));
    }

    @Test
    void testCheckoutNoPromotions() {
        Product p1 = new Product(1L, "p-002", "Mouse", new BigDecimal("50.00"), BigDecimal.ZERO, "Tech");
        when(productRepository.findBySku("p-002")).thenReturn(Optional.of(p1));
        
        CartItem item = new CartItem();
        item.setSku("p-002");
        item.setQuantity(2);
        List<CartItem> items = List.of(item);

        when(promotionRepository.findAll()).thenReturn(Collections.emptyList());
        when(shippingService.calculateCost(any())).thenReturn(new BigDecimal("10.00"));
        
        when(paymentMethodRepository.findByType(PaymentMethodType.CREDIT_CARD)).thenReturn(Optional.empty()); 

        CheckoutResult result = checkoutService.processCheckout(items, null, "CREDIT_CARD");

        assertEquals(0, new BigDecimal("100").compareTo(result.getSubtotal()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getProductDiscount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getPromotionalDiscount()));
        assertEquals(0, new BigDecimal("10").compareTo(result.getShippingCost()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getPaymentDiscount()));
        assertEquals(0, new BigDecimal("110").compareTo(result.getTotal()));
    }
    @Test
    void testCheckoutWithInactivePromotion() {
        Product p1 = new Product(1L, "p-001", "Laptop", new BigDecimal("1000.00"), BigDecimal.ZERO, "Tech"); 
        when(productRepository.findBySku("p-001")).thenReturn(Optional.of(p1));

        CartItem item = new CartItem();
        item.setSku("p-001");
        item.setQuantity(1);
        List<CartItem> items = List.of(item);

        // Promo is INACTIVE ('N')
        Promotion promo = new Promotion(1L, "Summer Sale", "TEN_PERCENT_OFF", new BigDecimal("0.00"), "N"); 
        when(promotionRepository.findAll()).thenReturn(List.of(promo));
        
        when(shippingService.calculateCost(any())).thenReturn(BigDecimal.ZERO);
        when(paymentMethodRepository.findByType(any())).thenReturn(Optional.empty());

        CheckoutResult result = checkoutService.processCheckout(items, null, "CASH");

        assertEquals(0, BigDecimal.ZERO.compareTo(result.getPromotionalDiscount()));
        assertEquals(0, new BigDecimal("1000").compareTo(result.getTotal()));
    }

    @Test
    void testProductNotFoundThrowsException() {
        when(productRepository.findBySku("unknown")).thenReturn(Optional.empty());
        
        CartItem item = new CartItem();
        item.setSku("unknown");
        List<CartItem> items = List.of(item);

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.processCheckout(items, null, "CASH");
        });
    }

    @Test
    void testInvalidPaymentMethodThrowsException() {
        Product p1 = new Product(1L, "p-001", "Laptop", new BigDecimal("1000.00"), BigDecimal.ZERO, "Tech"); 
        when(productRepository.findBySku("p-001")).thenReturn(Optional.of(p1));

        CartItem item = new CartItem();
        item.setSku("p-001");
        item.setQuantity(1);
        
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.processCheckout(List.of(item), null, "INVALID_METHOD");
        });
    }
}
