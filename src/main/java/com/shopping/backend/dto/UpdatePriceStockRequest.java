//Uc26+27+28

package com.shopping.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdatePriceStockRequest {
    private BigDecimal price;
    private Integer stock;
}
