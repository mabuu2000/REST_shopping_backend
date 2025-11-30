package com.shopping.backend.repo;

import com.shopping.backend.dto.ProductSaleItem;
import com.shopping.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("""
        SELECT new com.shopping.backend.dto.ProductSaleItem(
            oi.product.name,
            SUM(oi.quantity)
        )
        FROM OrderItem oi
        GROUP BY oi.product.name
    """)
    List<ProductSaleItem> getSalesSummary();
}
