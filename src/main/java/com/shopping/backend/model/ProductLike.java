package com.shopping.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_likes")
@Data
public class ProductLike {

    @EmbeddedId
    private ProductLikeKey id = new ProductLikeKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
}