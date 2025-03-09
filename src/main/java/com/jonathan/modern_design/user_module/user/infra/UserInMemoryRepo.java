package com.jonathan.modern_design.user_module.user.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.domain.model.User.UserId;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class UserInMemoryRepo implements UserRepo {
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
