package com.shopping.backend.service;

import com.shopping.backend.model.*;
import com.shopping.backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private ProductLikeRepository productLikeRepository; // New Repo
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    // uc10: like product (toggle) -> product_likes table, totalLikes counter
    @Transactional
    public String toggleLike(String username, UUID productId) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductLikeKey key = new ProductLikeKey();
        key.setUserId(user.getId());
        key.setProductId(product.getId());

        if (productLikeRepository.existsById(key)) {
            // unlike
            productLikeRepository.deleteById(key);
            if (product.getTotalLikes() > 0) {
                product.setTotalLikes(product.getTotalLikes() - 1);
            }
            productRepository.save(product);
            return "Product unliked.";
        } else {
            // like
            ProductLike like = new ProductLike();
            like.setId(key);
            like.setUser(user);
            like.setProduct(product);
            productLikeRepository.save(like);

            int currentLikes = (product.getTotalLikes() == null) ? 0 : product.getTotalLikes();
            product.setTotalLikes(currentLikes + 1);
            productRepository.save(product);
            return "Product liked.";
        }
    }

    // UC12: Add to Wishlist -> Affects wishlists table ONLY
    @Transactional
    public void addToWishlist(String username, UUID productId) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishlistKey key = new WishlistKey();
        key.setUserId(user.getId());
        key.setProductId(product.getId());

        if (wishlistRepository.existsById(key)) {
            throw new RuntimeException("Product is already in your wishlist.");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setId(key);
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }
}