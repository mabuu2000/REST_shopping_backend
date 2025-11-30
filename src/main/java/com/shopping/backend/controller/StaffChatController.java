//UC31

package com.shopping.backend.controller;

import com.shopping.backend.model.Message;
import com.shopping.backend.service.StaffChatService;
import com.shopping.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff/chat")
public class StaffChatController {

    private final StaffChatService chatService;

    @Autowired
    public StaffChatController(StaffChatService chatService) {
        this.chatService = chatService;
    }

    /** UC31: Staff views conversation with customer */
    @GetMapping("/{customerId}")
    public ResponseEntity<?> viewConversation(@PathVariable UUID customerId) {

        String staffEmail = AuthUtils.getCurrentUserEmail();

        List<Message> convo = chatService.viewConversation(customerId, staffEmail);

        return ResponseEntity.ok(convo);
    }

    /** UC31: Staff replies to customer */
    @PostMapping("/{customerId}")
    public ResponseEntity<?> sendReply(
            @PathVariable UUID customerId,
            @RequestBody String content
    ) {
        String staffEmail = AuthUtils.getCurrentUserEmail();

        Message msg = chatService.sendReply(customerId, content, staffEmail);

        return ResponseEntity.ok(msg);
    }
}
