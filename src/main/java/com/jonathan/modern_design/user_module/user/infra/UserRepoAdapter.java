package com.jonathan.modern_design.user_module.user.infra;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.user_module.user.domain.UserRepo;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserRepoAdapter implements UserRepo {
    private final UserSpringRepo repository;

    @Override
    public void registerUser(User user) {
        repository.save(user);
    }

    @Override
    public Optional<User> findByUuid(final User.UserId userId) {
        return repository.findByUuid(userId);
    }
}
