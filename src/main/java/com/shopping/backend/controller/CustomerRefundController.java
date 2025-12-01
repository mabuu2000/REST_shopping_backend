package com.shopping.backend.controller;

import com.shopping.backend.dto.RefundRequest;
import com.shopping.backend.dto.RefundResponse;
import com.shopping.backend.service.CustomerRefundService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/refund")
public class CustomerRefundController {

    private final CustomerRefundService refundService;

    @Autowired
    public CustomerRefundController(CustomerRefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<?> requestRefund(
            @PathVariable UUID orderId,
            @RequestBody RefundRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        RefundResponse response = refundService.requestRefund(orderId, req, email);

        return ResponseEntity.ok(response);
    }
}
