package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.UserRepository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getUuid(), user);
        return user;
    }
}
