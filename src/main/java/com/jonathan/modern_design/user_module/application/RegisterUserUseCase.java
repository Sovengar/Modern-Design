package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.user_module.domain.User;

public interface RegisterUserUseCase {
    User registerUser(RegisterUserCommand command);
}
