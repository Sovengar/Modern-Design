package com.jonathan.modern_design.user_module.domain;

import com.jonathan.modern_design.user_module.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User createUser(User user);

    Optional<User> findById(UUID uuid);

    default User findByIdOrElseThrow(UUID uuid) {
        return findById(uuid).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
