//Uc29,30,32,33


package com.shopping.backend.service;

import com.shopping.backend.dto.ShipmentRequest;
import com.shopping.backend.dto.UpdateOrderStatusRequest;
import com.shopping.backend.model.Order;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.OrderRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StaffOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public StaffOrderService(OrderRepository orderRepository,
                             UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /** Ensure user is staff */
    private User ensureStaff(String email) {
        User u = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"staff".equalsIgnoreCase(u.getRole())) {
            throw new RuntimeException("Permission denied: Staff only");
        }
        return u;
    }

    /** UC29: View all orders (staff) */
    public List<Order> listOrders(String email) {
        ensureStaff(email);
        return orderRepository.findAll();
    }

    /** UC29 / UC32 / UC33: View order details */
    public Order getOrderDetail(UUID orderId, String email) {
        ensureStaff(email);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /** UC30: Process order (update status) */
    public Order updateOrderStatus(UUID orderId, UpdateOrderStatusRequest req, String email) {

        ensureStaff(email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(req.getStatus());

        orderRepository.save(order);
        return order;
    }

    /** UC33: Arrange shipment (set tracking number + shipped status) */
    public Order arrangeShipment(UUID orderId, ShipmentRequest req, String email) {

        ensureStaff(email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setTrackingNumber(req.getTrackingNumber());
        order.setStatus("Shipped");

        orderRepository.save(order);
        return order;
    }
}
