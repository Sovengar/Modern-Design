package com.jonathan.modern_design.user_module;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
