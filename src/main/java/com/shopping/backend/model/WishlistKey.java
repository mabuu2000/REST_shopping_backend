package com.shopping.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@EqualsAndHashCode
public class WishlistKey implements Serializable {
    private UUID userId;
    private UUID productId;
}