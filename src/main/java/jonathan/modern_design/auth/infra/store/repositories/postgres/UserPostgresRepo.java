package jonathan.modern_design.auth.infra.store.repositories.postgres;

import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.UserRepo;
import jonathan.modern_design.auth.infra.store.repositories.spring_jpa.UserSpringJpaRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@DataAdapter
@RequiredArgsConstructor
class UserPostgresRepo implements UserRepo {
    private final UserSpringJpaRepo repository;

    @Override
    public void registerUser(User user) {
        repository.save(user);
    }

    @Override
    public void updateUser(final User user) {
        repository.save(user);
    }

    @Override
    public Optional<User> findById(final User.Id userId) {
        return repository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(final User.Id userId) {
        findById(userId).ifPresent(user -> {
            user.delete();
            repository.save(user);
        });
    }
}
