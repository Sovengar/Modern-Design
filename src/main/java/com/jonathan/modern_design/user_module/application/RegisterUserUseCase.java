package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design._shared.country.Country;
import com.jonathan.modern_design.user_module.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface RegisterUserUseCase {
    User.ID registerUser(RegisterUserCommand command);

    record RegisterUserCommand(UUID uuid, Optional<String> realname, String username, String email, String password, Country country) {
    }
}
