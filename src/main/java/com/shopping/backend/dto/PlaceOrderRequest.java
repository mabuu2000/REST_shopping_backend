package com.shopping.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PlaceOrderRequest {
    // uc15 shipping address by address's id
    @NotNull(message = "Address ID is required")
    private UUID addressId;

    // uc16 select shipping option ("Standard" or "Express")
    @NotBlank(message = "Shipping option is required")
    private String shippingOption;

    // uc17 payment method by card's id
    @NotNull(message = "Card ID is required")
    private UUID cardId;
}