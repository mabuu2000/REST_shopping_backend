package com.shopping.backend.dto;

import lombok.Data;

@Data
public class ReviewResponse {
    private String reviewId;
    private int rating;
    private String comment;
}
