package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final SpringUserRepository repository;
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        var userEntity = userMapper.toUserEntity(user);
        userEntity = repository.save(userEntity);
        return userMapper.toUser(userEntity);
    }
}
