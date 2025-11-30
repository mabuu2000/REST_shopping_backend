package com.shopping.backend.repo;

import com.shopping.backend.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {
    Optional<Refund> findByOrderId(UUID orderId);
}
