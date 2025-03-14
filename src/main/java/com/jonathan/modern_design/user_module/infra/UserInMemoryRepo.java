package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.domain.UserRepo;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
class UserInMemoryRepo implements UserRepo {
    private final ConcurrentHashMap<UserId, User> users = new ConcurrentHashMap<>();

    @Override
    public void registerUser(User user) {
        users.put(user.getUuid(), user);
    }

    @Override
    public Optional<User> findByUuid(final UserId userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
