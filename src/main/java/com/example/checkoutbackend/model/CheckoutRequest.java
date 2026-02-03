package com.example.checkoutbackend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private String cartId;
    
    @NotNull
    private List<@Valid CartItem> items;
    
    private ShippingAddress shippingAddress;

    @NotNull
    private String paymentMethod;
}
