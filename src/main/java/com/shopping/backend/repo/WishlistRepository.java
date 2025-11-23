package com.shopping.backend.repo;

import com.shopping.backend.model.User;
import com.shopping.backend.model.Wishlist;
import com.shopping.backend.model.WishlistKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistKey> {
    List<Wishlist> findAllByUser(User user);
}