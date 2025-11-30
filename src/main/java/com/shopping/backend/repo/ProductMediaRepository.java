//Uc25

package com.shopping.backend.repo;

import com.shopping.backend.model.ProductMedia;
import com.shopping.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, UUID> {

    // Lấy tất cả media của 1 product
    List<ProductMedia> findAllByProduct(Product product);
}
