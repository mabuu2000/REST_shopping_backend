package com.shopping.backend.service;

import com.shopping.backend.dto.ProductCreateRequest;
import com.shopping.backend.dto.UpdateDescriptionRequest;
import com.shopping.backend.dto.UpdatePriceStockRequest;
import com.shopping.backend.dto.UpdateProductRequest;
import com.shopping.backend.model.Product;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.ProductRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StaffProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public StaffProductService(UserRepository userRepository,
                               ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /** Check staff role */
    private User ensureSeller(String email) {
        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"staff".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Permission denied: Staff only");
        }

        return user;
    }

    /** UC24: Add product listing */
    public Product createProduct(ProductCreateRequest req, String email) {

        User seller = ensureSeller(email);

        if (req.getName() == null || req.getName().isEmpty())
            throw new RuntimeException("Product name is required");

        if (req.getPrice() == null || req.getPrice().doubleValue() <= 0)
            throw new RuntimeException("Product price must be > 0");

        if (req.getStock() == null || req.getStock() < 0)
            throw new RuntimeException("Product stock must be >= 0");

        if (req.getCategory() == null || req.getCategory().isEmpty())
            throw new RuntimeException("Category is required");

        Product p = new Product();
        p.setName(req.getName());
        p.setCategory(req.getCategory());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        p.setDescription(req.getDescription());
        p.setSeller(seller);

        productRepository.save(p);
        return p;
    }

    /** UC26: Set price & stock */
    public Product updatePriceAndStock(UUID productId, UpdatePriceStockRequest req, String email) {

        ensureSeller(email);

        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (req.getPrice() == null || req.getPrice().doubleValue() <= 0)
            throw new RuntimeException("Price must be > 0");

        if (req.getStock() == null || req.getStock() < 0)
            throw new RuntimeException("Stock cannot be negative");

        p.setPrice(req.getPrice());
        p.setStock(req.getStock());

        productRepository.save(p);
        return p;
    }

    /** UC27: Write / Update product description */
    public Product updateDescription(UUID productId, UpdateDescriptionRequest req, String email) {

        ensureSeller(email);

        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (req.getDescription() != null && req.getDescription().length() > 2000)
            throw new RuntimeException("Description too long (max 2000 chars)");

        p.setDescription(req.getDescription());

        productRepository.save(p);
        return p;
    }

    /** UC28: Update entire listing */
    public Product updateProduct(UUID productId, UpdateProductRequest req, String email) {

        ensureSeller(email);

        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (req.getName() != null && !req.getName().isBlank())
            p.setName(req.getName());

        if (req.getCategory() != null && !req.getCategory().isBlank())
            p.setCategory(req.getCategory());

        if (req.getPrice() != null && req.getPrice().doubleValue() > 0)
            p.setPrice(req.getPrice());

        if (req.getStock() != null && req.getStock() >= 0)
            p.setStock(req.getStock());

        if (req.getDescription() != null)
            p.setDescription(req.getDescription());

        productRepository.save(p);
        return p;
    }

    /** UC28: Delete product listing */
    public void deleteProduct(UUID productId, String email) {

        ensureSeller(email);

        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(p);
    }
}
