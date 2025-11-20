package com.shopping.backend.controller;

import com.shopping.backend.dto.LoginResponse;
import com.shopping.backend.dto.RegisterRequest;
import com.shopping.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.shopping.backend.dto.LoginRequest;
import com.shopping.backend.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        // If password weak or phone wrong this run this
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            String response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            // Handle duplicate
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed, please try again later");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        try {
            // Returns Token + User
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if ("User not found".equals(e.getMessage())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            if ("Incorrect password".equals(e.getMessage())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}