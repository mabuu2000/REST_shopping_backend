package com.shopping.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String category;

    @Column(name = "total_likes")
    private Integer totalLikes;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
}