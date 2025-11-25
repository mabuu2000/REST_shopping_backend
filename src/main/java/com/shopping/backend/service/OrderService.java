package com.shopping.backend.service;

import com.shopping.backend.dto.PlaceOrderRequest;
import com.shopping.backend.model.*;
import com.shopping.backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopping.backend.dto.OrderResponse;

import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(String username, PlaceOrderRequest request) {
        // get user
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // check wishlist
        List<Wishlist> wishlistItems = wishlistRepository.findAllByUser(user);
        if (wishlistItems.isEmpty()) {
            throw new RuntimeException("Wishlist is empty. Cannot place order.");
        }
        // get address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        // get cards
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // calculate total and check for stock
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Wishlist item : wishlistItems) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new RuntimeException("Product " + item.getProduct().getName() + " is out of stock.");
            }
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
        }

        // shipping fee
        BigDecimal shippingFee;
        if ("Express".equalsIgnoreCase(request.getShippingOption())) {
            shippingFee = new BigDecimal("14.00"); // express
        } else {
            shippingFee = new BigDecimal("5.00");  // standard
        }

        // create the order object
        Order order = new Order();
        order.setUser(user);
        order.setStatus("Processing");
        order.setShippingAddress(address.getAddressText());
        order.setPaymentMethod("Card ending in " + card.getCardNumber().substring(12));
        order.setTotalAmount(subtotal.add(shippingFee));

        // save order
        Order savedOrder = orderRepository.save(order);

        // remove item from wishlist, add to OrderItems and minus the stock
        for (Wishlist w : wishlistItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(w.getProduct());
            orderItem.setQuantity(w.getQuantity());
            orderItem.setPriceAtPurchase(w.getProduct().getPrice()); // save the củrent price

            orderItemRepository.save(orderItem);

            // minus the stock
            Product p = w.getProduct();
            p.setStock(p.getStock() - w.getQuantity());
            productRepository.save(p);
        }
        // empty wishlist
        wishlistRepository.deleteAll(wishlistItems);

        return savedOrder;
    }

    public List<OrderResponse> getMyOrders(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // get the order from supabase
        List<Order> orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user);
        return orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setOrderId(order.getId());
            response.setStatus(order.getStatus());
            response.setTotalAmount(order.getTotalAmount());
            response.setShippingAddress(order.getShippingAddress());
            response.setDateOrdered(order.getCreatedAt());

            List<OrderResponse.OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
                OrderResponse.OrderItemDto dto = new OrderResponse.OrderItemDto();
                dto.setProductName(item.getProduct().getName());
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPriceAtPurchase());

                // get the first image if available
                if (item.getProduct().getMedia() != null && !item.getProduct().getMedia().isEmpty()) {
                    dto.setImageUrl(item.getProduct().getMedia().get(0).getUrl());
                }

                return dto;
            }).collect(Collectors.toList());

            response.setItems(itemDtos);
            return response;
        }).collect(Collectors.toList());
    }
}