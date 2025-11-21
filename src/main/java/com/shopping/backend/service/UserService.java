package com.shopping.backend.service;

import com.shopping.backend.dto.AccountResponse;
import com.shopping.backend.dto.UpdateAccountRequest;
import com.shopping.backend.model.Address;
import com.shopping.backend.model.User;
import com.shopping.backend.repo.AddressRepository;
import com.shopping.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService { // It is a class, NOT an interface

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    // view account
    public AccountResponse getAccountInfo(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Address> addresses = addressRepository.findAllByUser(user);

        AccountResponse response = new AccountResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());

        response.setAddresses(addresses.stream()
                .map(Address::getAddressText)
                .collect(Collectors.toList()));

        return response;
    }

    // update account
    public AccountResponse updateAccount(String username, UpdateAccountRequest request) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);

        if (request.getNewAddress() != null && !request.getNewAddress().isBlank()) {
            Address address = new Address();
            address.setUser(user);
            address.setAddressText(request.getNewAddress());
            addressRepository.save(address);
        }

        return getAccountInfo(username);
    }
}