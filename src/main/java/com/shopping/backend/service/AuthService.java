package com.shopping.backend.service;

import com.shopping.backend.dto.RegisterRequest;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.shopping.backend.util.Jwt;
import com.shopping.backend.dto.LoginResponse;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Jwt jwt;

    public String register(RegisterRequest request) {
        // Check username/email exist
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        // Force role to customer
        user.setRole("customer");

        // Hash
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "Account created successfully";
    }

    public LoginResponse login(com.shopping.backend.dto.LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        // GENERATE TOKEN
        String token = jwt.generateToken(user.getUsername());

        // Return Token AND User
        return new LoginResponse(token, user);
    }
}