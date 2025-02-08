package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.shared.annotations.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserRepositorySpringAdapter implements UserRepository {
    private final SpringUserRepository repository;
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        final var userEntity = repository.save(userMapper.toUserEntity(user));
        return userMapper.toUser(userEntity);
    }
}
