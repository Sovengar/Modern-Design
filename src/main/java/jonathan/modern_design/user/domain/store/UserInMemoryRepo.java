package jonathan.modern_design.user.domain.store;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design.user.domain.models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
public class UserInMemoryRepo implements UserRepo {
    private final ConcurrentHashMap<User.Id, User> users = new ConcurrentHashMap<>();

    @Override
    public void registerUser(User user) {
        users.put(user.uuid(), user);
    }

    @Override
    public void updateUser(final User user) {
        users.put(user.uuid(), user);
    }

    @Override
    public Optional<User> findByUuid(final User.Id userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }
}
