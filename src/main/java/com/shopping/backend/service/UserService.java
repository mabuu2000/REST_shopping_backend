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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

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

        List<AccountResponse.AddressDto> addressDtos = addresses.stream()
                .map(addr -> new AccountResponse.AddressDto(addr.getId(), addr.getAddressText()))
                .collect(Collectors.toList());

        response.setAddresses(addressDtos);
        return response;
    }

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

    public void deleteAddress(String username, UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this address");
        }

        addressRepository.delete(address);
    }
}