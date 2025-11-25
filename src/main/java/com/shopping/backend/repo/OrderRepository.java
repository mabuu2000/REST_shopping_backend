package com.shopping.backend.repo;

import com.shopping.backend.model.Order;
import com.shopping.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUserOrderByCreatedAtDesc(User user);
}