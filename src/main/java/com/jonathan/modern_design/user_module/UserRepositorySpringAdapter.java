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
        var userEntity = userMapper.toUserEntity(user);
        userEntity = repository.save(userEntity);
        return userMapper.toUser(userEntity);
    }
}
