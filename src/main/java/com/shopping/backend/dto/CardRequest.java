package com.shopping.backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardRequest {
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits and be numbers")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Expiry must be MM/YY")
    private String expiry;

    @Pattern(regexp = "^\\d{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;
}
