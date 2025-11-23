package com.shopping.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageRequest {
    @NotNull(message = "Receiver ID is required")
    private UUID receiverId;
    @NotBlank(message = "Message cannot be empty")
    private String content;
}