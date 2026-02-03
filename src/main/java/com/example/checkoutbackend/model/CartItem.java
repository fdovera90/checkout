package com.example.checkoutbackend.model;

import lombok.Data;

@Data
public class CartItem {
    private String sku;
    private int quantity;
}
