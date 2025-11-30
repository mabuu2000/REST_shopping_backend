package com.shopping.backend.controller;

import com.shopping.backend.dto.ReviewRequest;
import com.shopping.backend.dto.ReviewResponse;
import com.shopping.backend.service.CustomerReviewService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/review")
public class CustomerReviewController {

    private final CustomerReviewService reviewService;

    @Autowired
    public CustomerReviewController(CustomerReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{orderId}/{productId}")
    public ResponseEntity<?> submitReview(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @RequestBody ReviewRequest req) {

        String email = AuthUtils.getCurrentUserEmail();

        ReviewResponse response =
                reviewService.submitReview(orderId, productId, req, email);

        return ResponseEntity.ok(response);
    }
}
