package com.shopping.backend.service;

import com.shopping.backend.model.*;
import com.shopping.backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopping.backend.dto.WishlistResponse;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.List;

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

    // view wishlist
    public WishlistResponse getMyWishlist(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Wishlist> items = wishlistRepository.findAllByUser(user);

        WishlistResponse response = new WishlistResponse();
        BigDecimal total = BigDecimal.ZERO;
        List<WishlistResponse.WishlistItemDto> dtos = items.stream().map(item -> {
            WishlistResponse.WishlistItemDto dto = new WishlistResponse.WishlistItemDto();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setStockAvailable(item.getProduct().getStock());
            return dto;
        }).collect(Collectors.toList());
        // total: sum(price*quantity)
        for (Wishlist item : items) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }

        response.setItems(dtos);
        response.setTotalCost(total);
        return response;
    }

    // update quantity
    @Transactional
    public void updateQuantity(String username, UUID productId, Integer newQuantity) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WishlistKey key = new WishlistKey();
        key.setUserId(user.getId());
        key.setProductId(productId);

        Wishlist wishlist = wishlistRepository.findById(key)
                .orElseThrow(() -> new RuntimeException("Item not in wishlist"));
        // check stock
        if (newQuantity > wishlist.getProduct().getStock()) {
            throw new RuntimeException("Only " + wishlist.getProduct().getStock() + " items in stock.");
        }
        if (newQuantity <= 0) {
            throw new RuntimeException("Quantity must be at least 1");
        }
        wishlist.setQuantity(newQuantity);
        wishlistRepository.save(wishlist);
    }

    // remove item
    @Transactional
    public void removeItem(String username, UUID productId) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        WishlistKey key = new WishlistKey();
        key.setUserId(user.getId());
        key.setProductId(productId);
        if (!wishlistRepository.existsById(key)) {
            throw new RuntimeException("Item not found in wishlist");
        }

        wishlistRepository.deleteById(key);
    }
}