package com.shopping.backend.controller;

import com.shopping.backend.model.Product;
import com.shopping.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

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
}