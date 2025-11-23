package com.shopping.backend.service;

import com.shopping.backend.model.Product;
import com.shopping.backend.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> browse(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.filterProducts(category, minPrice, maxPrice);
    }
}