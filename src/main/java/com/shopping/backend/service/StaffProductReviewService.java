package com.shopping.backend.service;

import com.shopping.backend.dto.StaffReplyRequest;
import com.shopping.backend.model.Review;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.ReviewRepository;
import com.shopping.backend.repo.UserRepository;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StaffProductReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public StaffProductReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    /** Check staff role */
    private User validateStaff(String email) {
        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"staff".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Permission denied: Staff only");
        }

        return user;
    }

    /** UC23: Staff replies to product review */
    public Review replyToReview(UUID reviewId, StaffReplyRequest request, String email) {

        // Ensure logged-in user is staff
        validateStaff(email);

        // Review exists?
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Already replied?
        if (review.getStaffReply() != null) {
            throw new RuntimeException("This review already has a staff reply.");
        }

        // Set reply
        review.setStaffReply(request.getReply());

        // Save review
        reviewRepository.save(review);

        // Log activity (simple)
        System.out.println("STAFF RESPONSE LOG — Review " + review.getId() +
                " replied by staff: " + email);

        return review;
    }
}
