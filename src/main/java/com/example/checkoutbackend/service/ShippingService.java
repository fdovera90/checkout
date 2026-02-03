package com.example.checkoutbackend.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ShippingService {

    public BigDecimal calculateCost(String zoneId) {
        if (zoneId == null || zoneId.trim().isEmpty()) return BigDecimal.ZERO;
        return switch (zoneId.toLowerCase()) {
            case "zone-1" -> new BigDecimal("10"); // Santiago Centró
            case "zone-2" -> new BigDecimal("25"); // Ñuñoa / Providencia
            case "zone-3" -> new BigDecimal("50"); // Las Condes / Barnechea
            case "zone-4" -> new BigDecimal("100"); // Regiones Cercanas
            case "zone-5" -> new BigDecimal("150"); // Extremo Norte/Sur
            default -> BigDecimal.ZERO;
        };
    }

    public java.util.Map<String, String> getAvailableZones() {
        java.util.LinkedHashMap<String, String> zones = new java.util.LinkedHashMap<>();
        zones.put("zone-1", "Santiago Centro");
        zones.put("zone-2", "Ñuñoa / Providencia");
        zones.put("zone-3", "Las Condes / Barnechea");
        zones.put("zone-4", "Regiones Cercanas");
        zones.put("zone-5", "Extremo Norte/Sur");
        return zones;
    }
}
