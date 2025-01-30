package com.jonathan.modern_design.user_module.application.create_user;

import com.jonathan.modern_design.common.UseCase;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {
    private final UserRepository repository;

    @Override
    public User createUser() {
        return repository.createUser();
    }
}
