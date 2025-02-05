package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;

interface CreateUserUseCase {
    User createUser(CreateUserCommand command);
}
