package com.jonathan.modern_design.user_module.user.domain;

import com.jonathan.modern_design.user_module.user.domain.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public interface UserRepo {
    User createUser(User user);

    Optional<User> findByUuid(User.ID id);

    default User findByUUIDOrElseThrow(User.ID id) {
        return findByUuid(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
