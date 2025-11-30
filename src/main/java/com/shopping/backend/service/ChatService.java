package com.shopping.backend.service;

import com.shopping.backend.dto.MessageRequest;
import com.shopping.backend.model.Message;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.MessageRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    // send msg
    public void sendMessage(String senderUsername, MessageRequest request) {
        User sender = userRepository.findByUsernameOrEmail(senderUsername, senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("User not found or unavailable."));
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());

        messageRepository.save(message);
    }

    // get chat history
    public List<Message> getChatHistory(String username, UUID otherUserId) {
        User currentUser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));
        return messageRepository.findConversationByUsers(currentUser, otherUser);
    }
}