package com.shopping.backend.repo;


import com.shopping.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}

