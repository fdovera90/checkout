package com.example.checkoutbackend.controller;

import com.example.checkoutbackend.model.PaymentMethod;
import com.example.checkoutbackend.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentMethodController { // Named it ProductController by accident in previous thought, fixing it here to PaymentMethodController

    private final PaymentMethodRepository paymentMethodRepository;

    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }
}
