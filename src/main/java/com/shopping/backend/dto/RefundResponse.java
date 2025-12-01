package com.shopping.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundResponse {
    private String refundId;
    private String orderId;
    private String status;
    private String reason;
}
