package com.example.checkoutbackend.service;

import com.example.checkoutbackend.model.ShippingZone;
import com.example.checkoutbackend.repository.ShippingZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingZoneRepository shippingZoneRepository;

    public BigDecimal calculateCost(String zoneId) {
        if (zoneId == null || zoneId.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return shippingZoneRepository.findById(zoneId.toLowerCase())
                .map(ShippingZone::getCost)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shipping zone: " + zoneId));
    }

    public Map<String, String> getAvailableZones() {
        return shippingZoneRepository.findAll().stream()
                .collect(Collectors.toMap(ShippingZone::getId, ShippingZone::getName));
    }
}
