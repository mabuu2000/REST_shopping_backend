package com.shopping.backend.dto;

import lombok.Data;

@Data
public class RefundResponse {
    private String refundId;
    private String orderId;
    private String status;
    private String reason;
}
