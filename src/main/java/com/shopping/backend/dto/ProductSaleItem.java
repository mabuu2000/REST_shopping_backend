package com.shopping.backend.dto;

import lombok.Data;

@Data
public class ProductSaleItem {
    private String productName;
    private long quantitySold;

    public ProductSaleItem(String productName, long quantitySold) {
        this.productName = productName;
        this.quantitySold = quantitySold;
    }
}
