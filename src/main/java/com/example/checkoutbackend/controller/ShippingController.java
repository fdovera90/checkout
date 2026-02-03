package com.example.checkoutbackend.controller;

import com.example.checkoutbackend.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/shipping-zones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping
    public Map<String, String> getZones() {
        return shippingService.getAvailableZones();
    }
}
