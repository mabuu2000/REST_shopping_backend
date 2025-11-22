package com.shopping.backend.service;

import com.shopping.backend.dto.CardRequest;
import com.shopping.backend.model.Card;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.CardRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;


    private User getUser(String username) {
        return userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new RuntimeException("User not found."));
    }

    public List<Card> getCard(String username) {
        return cardRepository.findAllByUser(getUser(username));
    }

    public Card addCard(String username, CardRequest request) {
        Card card = new Card();
        card.setUser(getUser(username));
        card.setCardNumber(request.getCardNumber());
        card.setExpiry(request.getExpiry());
        card.setCvv(request.getCvv());
        return cardRepository.save(card);
    }

    public void deleteCard(String username, UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // does the card belong to the user
        if (!card.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this card");
        }

        cardRepository.delete(card);
    }
}
