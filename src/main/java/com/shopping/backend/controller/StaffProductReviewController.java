package com.shopping.backend.controller;

import com.shopping.backend.dto.StaffReplyRequest;
import com.shopping.backend.model.Review;
import com.shopping.backend.service.StaffProductReviewService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff/review")
public class StaffProductReviewController {

    private final StaffProductReviewService service;

    @Autowired
    public StaffProductReviewController(StaffProductReviewService service) {
        this.service = service;
    }

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<?> replyToReview(
            @PathVariable UUID reviewId,
            @RequestBody StaffReplyRequest request
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Review updated = service.replyToReview(reviewId, request, email);

        return ResponseEntity.ok(updated);
    }
}
