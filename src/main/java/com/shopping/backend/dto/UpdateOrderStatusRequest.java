//Uc30
package com.shopping.backend.dto;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private String status;  // Processing, Shipped, Delivered, Cancelled
}

