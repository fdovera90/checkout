package com.example.checkoutbackend.controller;

import com.example.checkoutbackend.model.CheckoutRequest;
import com.example.checkoutbackend.model.CheckoutResult;
import com.example.checkoutbackend.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResult> checkout(@Valid @RequestBody CheckoutRequest request) {
        log.info("Received checkout request for cart: {}", request.getCartId());
        CheckoutResult result = checkoutService.processCheckout(request.getItems(), request.getShippingAddress(), request.getPaymentMethod());
        log.info("Checkout processed successfully for cart: {}. Total: {}", request.getCartId(), result.getTotal());
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Validation error during checkout: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
