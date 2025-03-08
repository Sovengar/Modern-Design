package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.user_module.domain.UserRepository;
import com.jonathan.modern_design.user_module.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final UserSpringRepo repository;

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findByUuid(final UUID uuid) {
        return repository.findByUuid(uuid);
    }


}
