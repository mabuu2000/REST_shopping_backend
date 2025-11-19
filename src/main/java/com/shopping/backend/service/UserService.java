package com.shopping.backend.service;

import java.util.List;
import com.shopping.backend.model.User;

public interface UserService {
    List<User> getAllUsers();
}