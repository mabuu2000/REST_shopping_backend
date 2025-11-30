package com.shopping.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
}
