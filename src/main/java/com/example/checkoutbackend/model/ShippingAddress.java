package com.example.checkoutbackend.model;

import lombok.Data;

@Data
public class ShippingAddress {
    private String street;
    private String city;
    private String zoneId;
}
