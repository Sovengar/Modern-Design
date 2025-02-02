package com.jonathan.modern_design.user_module;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UserRepositorySpringAdapter implements UserRepository {
    private final SpringUserRepository springUserRepository;

    @Override
    public User createUser(User user) {
        return null; //TODO FIX
    }
}
