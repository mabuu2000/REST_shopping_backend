package com.shopping.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class AccountResponse {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private List<String> addresses;
}