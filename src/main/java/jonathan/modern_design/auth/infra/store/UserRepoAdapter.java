package jonathan.modern_design.auth.infra.store;

import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface UserRepoSpringDataJPA extends JpaRepository<User, User.Id> {
}

@DataAdapter
@RequiredArgsConstructor
class UserRepoAdapter implements UserRepo {
    private final UserRepoSpringDataJPA repository;

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
