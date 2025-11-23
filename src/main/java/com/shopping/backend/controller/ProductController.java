package com.shopping.backend.controller;

import com.shopping.backend.model.Product;
import com.shopping.backend.service.ProductService;
import com.shopping.backend.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Please enter a keyword.");
        }

        if (!keyword.matches("[a-zA-Z0-9 ]+")) {
            return ResponseEntity.badRequest().body("Special characters are not allowed.");
        }

        List<Product> results = productService.search(keyword);

        if (results.isEmpty()) {
            return ResponseEntity.ok("No products match your search.");
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<?> browseProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        // if min <0 then set null
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            minPrice = null;
        }
        // same for max
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            maxPrice = null;
        }
        // if min > max ignore max
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            maxPrice = null;
        }
        List<Product> products = productService.browse(category, minPrice, maxPrice);
        if (products.isEmpty()) {
            return ResponseEntity.ok("No products found.");
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable UUID id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            // product unavailable/deleted
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unable to load product details.");
        }
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeProduct(@PathVariable UUID id) {
        System.out.println("DEBUG: Reached Like Controller"); // Debugging line
        String username = getCurrentUsername();
        try {
            String response = wishlistService.toggleLike(username, id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // uc12: add to wishlist
    @PostMapping("/{id}/wishlist")
    public ResponseEntity<?> addProductToWishlist(@PathVariable UUID id) {
        String username = getCurrentUsername();
        try {
            wishlistService.addToWishlist(username, id);
            return ResponseEntity.ok("Product added to wishlist.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}