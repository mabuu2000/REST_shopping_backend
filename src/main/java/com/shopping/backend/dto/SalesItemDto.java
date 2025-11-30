package com.shopping.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesItemDto {
    private String productName;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
}
