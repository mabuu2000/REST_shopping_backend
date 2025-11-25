package com.shopping.backend.controller;

import com.shopping.backend.dto.PlaceOrderRequest;
import com.shopping.backend.model.Order;
import com.shopping.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.shopping.backend.dto.OrderResponse;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // place order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        try {
            String username = getCurrentUsername();
            Order order = orderService.placeOrder(username, request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(orderService.getMyOrders(username));
    }
}