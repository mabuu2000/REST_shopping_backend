package com.shopping.backend.repo;

import com.shopping.backend.model.Card;
import com.shopping.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    List<Card> findAllByUser(User user);
}
