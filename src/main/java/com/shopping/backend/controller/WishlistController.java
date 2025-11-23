package com.shopping.backend.controller;

import com.shopping.backend.dto.WishlistResponse;
import com.shopping.backend.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // view wishlist
    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist() {
        return ResponseEntity.ok(wishlistService.getMyWishlist(getCurrentUsername()));
    }

    // update quantity, put
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateQuantity(@PathVariable UUID productId, @RequestParam Integer quantity) {
        try {
            wishlistService.updateQuantity(getCurrentUsername(), productId, quantity);
            return ResponseEntity.ok("Quantity updated.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove item
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeItem(@PathVariable UUID productId) {
        wishlistService.removeItem(getCurrentUsername(), productId);
        return ResponseEntity.ok("Item removed.");
    }
}