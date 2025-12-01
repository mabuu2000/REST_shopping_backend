package com.shopping.backend.service;

import com.shopping.backend.dto.RefundRequest;
import com.shopping.backend.dto.RefundResponse;
import com.shopping.backend.model.Order;
import com.shopping.backend.model.Refund;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.OrderRepository;
import com.shopping.backend.repo.RefundRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerRefundService {

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerRefundService(OrderRepository orderRepository,
                                 RefundRepository refundRepository,
                                 UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.refundRepository = refundRepository;
        this.userRepository = userRepository;
    }

    public RefundResponse requestRefund(UUID orderId, RefundRequest req, String email) {

        User user = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check order existence
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check owner
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied: Not your order");
        }

        // Check delivered
        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order not eligible for refund");
        }

        // Check duplicate refund
        if (refundRepository.findByOrder(order).isPresent()) {
            throw new RuntimeException("Refund already requested for this order");
        }

        refundRepository.findByOrder(order)
                .ifPresent(r -> System.out.println("Refund FOUND -> ID = " + r.getId()));



        // Create refund
        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setReason(req.getReason());
        refund.setStatus("Pending");

        refundRepository.save(refund);
        order.setStatus("Refunded");
        orderRepository.save(order);


        return new RefundResponse(
                refund.getId().toString(),
                orderId.toString(),
                "Pending",
                req.getReason()


        );
    }
}

