package com.shopping.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class WishlistResponse {
    private List<WishlistItemDto> items;
    private BigDecimal totalCost;

    @Data
    public static class WishlistItemDto {
        private UUID productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private Integer stockAvailable; // frontend show max limit
    }
}