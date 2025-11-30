//Uc24, 26,27,28

package com.shopping.backend.controller;

import com.shopping.backend.dto.ProductCreateRequest;
import com.shopping.backend.dto.UpdateDescriptionRequest;
import com.shopping.backend.dto.UpdatePriceStockRequest;
import com.shopping.backend.dto.UpdateProductRequest;
import com.shopping.backend.model.Product;
import com.shopping.backend.service.StaffProductService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/staff/products")
public class StaffProductController {

    private final StaffProductService productService;

    @Autowired
    public StaffProductController(StaffProductService productService) {
        this.productService = productService;
    }

    /** UC24: Create new product */
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest req) {

        String email = AuthUtils.getCurrentUserEmail();

        Product created = productService.createProduct(req, email);

        return ResponseEntity.ok(created);
    }

    /** UC26: Update price & stock */
    @PostMapping("/{productId}/pricing")
    public ResponseEntity<?> updatePricing(
            @PathVariable UUID productId,
            @RequestBody UpdatePriceStockRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Product updated = productService.updatePriceAndStock(productId, req, email);

        return ResponseEntity.ok(updated);
    }

    /** UC27: Update description */
    @PostMapping("/{productId}/description")
    public ResponseEntity<?> updateDescription(
            @PathVariable UUID productId,
            @RequestBody UpdateDescriptionRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Product updated = productService.updateDescription(productId, req, email);

        return ResponseEntity.ok(updated);
    }

    /** UC28: Update entire listing */
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID productId,
            @RequestBody UpdateProductRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Product updated = productService.updateProduct(productId, req, email);

        return ResponseEntity.ok(updated);
    }

    /** UC28: Delete listing */
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId) {

        String email = AuthUtils.getCurrentUserEmail();

        productService.deleteProduct(productId, email);

        return ResponseEntity.ok("Product deleted successfully.");
    }
}
