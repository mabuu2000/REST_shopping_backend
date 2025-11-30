//Uc29, 30, 32, 33

package com.shopping.backend.controller;

import com.shopping.backend.dto.ShipmentRequest;
import com.shopping.backend.dto.UpdateOrderStatusRequest;
import com.shopping.backend.model.Order;
import com.shopping.backend.service.StaffOrderService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff/orders")
public class StaffOrderController {

    private final StaffOrderService orderService;

    @Autowired
    public StaffOrderController(StaffOrderService orderService) {
        this.orderService = orderService;
    }

    /** UC29: View orders */
    @GetMapping
    public ResponseEntity<?> listOrders() {
        String email = AuthUtils.getCurrentUserEmail();

        List<Order> orders = orderService.listOrders(email);
        return ResponseEntity.ok(orders);
    }

    /** UC29 / UC32 / UC33: View order details */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable UUID orderId) {
        String email = AuthUtils.getCurrentUserEmail();

        Order order = orderService.getOrderDetail(orderId, email);
        return ResponseEntity.ok(order);
    }

    /** UC30: Process order (status update) */
    @PostMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Order updated = orderService.updateOrderStatus(orderId, req, email);
        return ResponseEntity.ok(updated);
    }

    /** UC33: Arrange shipment */
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<?> arrangeShipment(
            @PathVariable UUID orderId,
            @RequestBody ShipmentRequest req
    ) {
        String email = AuthUtils.getCurrentUserEmail();

        Order updated = orderService.arrangeShipment(orderId, req, email);
        return ResponseEntity.ok(updated);
    }
}

