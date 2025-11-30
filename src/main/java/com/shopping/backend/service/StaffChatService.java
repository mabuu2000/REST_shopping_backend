//Uc31
package com.shopping.backend.service;

import com.shopping.backend.model.Message;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.MessageRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StaffChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public StaffChatService(MessageRepository messageRepository,
                            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /** Check role staff */
    private User ensureStaff(String email) {
        User u = userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"staff".equalsIgnoreCase(u.getRole())) {
            throw new RuntimeException("Permission denied: Staff only");
        }

        return u;
    }

    /** UC31: Staff views messages from a customer */
    public List<Message> viewConversation(UUID customerId, String staffEmail) {

        ensureStaff(staffEmail);

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Find all messages between staff and customer
        return messageRepository.findConversationByEmails(staffEmail, customer.getEmail());
    }

    /** UC31: Staff replies to customer */
    public Message sendReply(UUID customerId, String content, String staffEmail) {

        User staff = ensureStaff(staffEmail);

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Message cannot be empty");
        }

        Message msg = new Message();
        msg.setSender(staff);
        msg.setReceiver(customer);
        msg.setContent(content);

        messageRepository.save(msg);

        return msg;
    }
}
