package com.shopping.backend.controller;

import com.shopping.backend.dto.CardRequest;
import com.shopping.backend.model.Card;
import com.shopping.backend.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<List<Card>> getCards() {
        return ResponseEntity.ok(cardService.getCard(getCurrentUsername()));
    }

    @PostMapping
    public ResponseEntity<Card> addCard(@Valid @RequestBody CardRequest request) {
        return ResponseEntity.ok(cardService.addCard(getCurrentUsername(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(getCurrentUsername(), id);
        return ResponseEntity.ok("Card deleted successfully");
    }
}