package com.shopping.backend.controller;

import com.shopping.backend.dto.MessageRequest;
import com.shopping.backend.model.Message;
import com.shopping.backend.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // send message
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody MessageRequest request) {
        try {
            String username = getCurrentUsername();
            chatService.sendMessage(username, request);
            return ResponseEntity.ok("Message sent successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // view chat his
    @GetMapping("/history/{otherUserId}")
    public ResponseEntity<List<Message>> getHistory(@PathVariable UUID otherUserId) {
        String username = getCurrentUsername();
        return ResponseEntity.ok(chatService.getChatHistory(username, otherUserId));
    }
}