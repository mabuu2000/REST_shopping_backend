package com.shopping.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AccountResponse {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;

    private List<AddressDto> addresses;

    @Data
    @AllArgsConstructor
    public static class AddressDto {
        private UUID id;
        private String text;
    }
}