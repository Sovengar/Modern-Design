package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class UserInMemoryRepo implements UserRepository {
    private final ConcurrentHashMap<User.ID, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getUuid(), user);
        return user;
    }

    @Override
    public Optional<User> findByUuid(final User.ID id) {
        return Optional.ofNullable(users.get(id));
    }
}
