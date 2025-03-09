package com.jonathan.modern_design.user_module.user.domain;

import com.jonathan.modern_design.user_module.user.domain.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public interface UserRepo {
    void registerUser(User user);

    Optional<User> findByUuid(User.UserId userId);

    default User findByUUIDOrElseThrow(User.UserId userId) {
        return findByUuid(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
