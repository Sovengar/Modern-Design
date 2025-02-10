package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.shared.annotations.Fake;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) {
        user.setUuid(UUID.randomUUID());

        users.put(user.getUuid(), user);
        return user;
    }
}
