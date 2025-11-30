//UC28

package com.shopping.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
}
