package com.shopping.backend.repo;

import com.shopping.backend.model.ProductLike;
import com.shopping.backend.model.ProductLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, ProductLikeKey> {
}