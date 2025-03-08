package com.jonathan.modern_design.user_module.domain;

import com.jonathan.modern_design.user_module.domain.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User createUser(User user);

    Optional<User> findByUuid(UUID uuid);

    default User findByUUIDOrElseThrow(UUID uuid) {
        return findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
