package com.jonathan.modern_design.user_module.application;

import com.jonathan.modern_design.user_module.domain.User;

public interface CreateUserUseCase {
    User createUser(CreateUserCommand command);
}
