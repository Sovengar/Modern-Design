package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.Builder;

import java.util.UUID;

public interface RegisterUserUseCase {
    User registerUser(RegisterUserCommand command);

    @Builder
    record RegisterUserCommand(UUID uuid, String realname, String email, String username, String password, String country) {
    }
}
