package jonathan.modern_design.auth.infra.store.repositories.inmemory;

import jonathan.modern_design._shared.tags.tests.Fake;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Fake
public class UserInMemoryRepo implements UserRepo {
    private final ConcurrentHashMap<User.Id, User> users = new ConcurrentHashMap<>();

    @Override
    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(final User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(final User.Id userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void delete(final User.Id userId) {
        users.remove(userId);
    }
}
