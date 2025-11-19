package com.shopping.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.shopping.backend.model.User;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // .findAll() is already provided by JpaRepository, no @Query needed.
}