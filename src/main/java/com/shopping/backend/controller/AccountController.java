package com.shopping.backend.controller;

import com.shopping.backend.dto.AccountResponse;
import com.shopping.backend.dto.UpdateAccountRequest;
import com.shopping.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    // get the username from the Token
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // view account information
    @GetMapping
    public ResponseEntity<AccountResponse> viewAccount() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(userService.getAccountInfo(username));
    }

    @PutMapping
    public ResponseEntity<?> updateAccount(@Valid @RequestBody UpdateAccountRequest request) {
        String username = getCurrentUsername();
        return ResponseEntity.ok(userService.updateAccount(username, request));
    }
}