package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.common.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
class CreateUserService implements CreateUserUseCase {
    private final UserRepository repository;

    @Override
    public User createUser(User user) {
        return repository.createUser(user);
    }
}
