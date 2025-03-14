package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import com.jonathan.modern_design.user_module.domain.UserRepo;
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
    public Optional<User> findByUuid(final UserId userId) {
        return repository.findByUuid(userId);
    }
}
