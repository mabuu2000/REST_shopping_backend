package com.shopping.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID orderId;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private LocalDateTime dateOrdered;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private String imageUrl;
    }
}