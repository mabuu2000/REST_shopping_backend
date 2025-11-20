package com.shopping.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wishlists")
@Data
public class Wishlist {

    @EmbeddedId
    private WishlistKey id = new WishlistKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
}