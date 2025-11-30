package com.shopping.backend.service;

import com.shopping.backend.dto.ReviewRequest;
import com.shopping.backend.dto.ReviewResponse;
import com.shopping.backend.model.Order;
import com.shopping.backend.model.Product;
import com.shopping.backend.model.Review;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.OrderRepository;
import com.shopping.backend.repo.ProductRepository;
import com.shopping.backend.repo.ReviewRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CustomerReviewService(OrderRepository orderRepository,
                                 ReviewRepository reviewRepository,
                                 UserRepository userRepository,
                                 ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ReviewResponse submitReview(UUID orderId,
                                       UUID productId,
                                       ReviewRequest req,
                                       String userEmail) {

        User user = userRepository.findByUsernameOrEmail(userEmail, userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied: Not your order");
        }

        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order not delivered yet");
        }

        if (reviewRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("You already reviewed this product");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(req.getRating());
        review.setComment(req.getComment());

        reviewRepository.save(review);

        ReviewResponse res = new ReviewResponse();
        res.setReviewId(review.getId().toString());
        res.setRating(review.getRating());
        res.setComment(review.getComment());

        return res;
    }
}
