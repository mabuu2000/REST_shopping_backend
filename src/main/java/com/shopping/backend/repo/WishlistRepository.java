package com.shopping.backend.repo;

import com.shopping.backend.model.Wishlist;
import com.shopping.backend.model.WishlistKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistKey> {
}