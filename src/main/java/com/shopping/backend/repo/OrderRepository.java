package com.shopping.backend.repo;

import com.shopping.backend.model.Order;
import com.shopping.backend.model.User;
import com.shopping.backend.dto.SalesItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserOrderByCreatedAtDesc(User user);

    @Query("""
        SELECT new com.shopping.backend.dto.SalesItemDto(
            oi.product.name,
            SUM(oi.quantity),
            SUM(oi.priceAtPurchase * oi.quantity)
        )
        FROM OrderItem oi
        JOIN oi.order o
        WHERE o.createdAt >= :startDate
        GROUP BY oi.product.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<SalesItemDto> getSalesStats(LocalDateTime startDate);
}
