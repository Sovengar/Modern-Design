package jonathan.modern_design.user.infra;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

interface UserSpringRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(User.UserId userId);
}

@Query
@RequiredArgsConstructor
class UserRepoAdapter implements UserRepo {
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


@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
class UserInMemoryRepo implements UserRepo {
    private final ConcurrentHashMap<User.UserId, User> users = new ConcurrentHashMap<>();

    @Override
    public void registerUser(User user) {
        users.put(user.uuid(), user);
    }

    @Override
    public Optional<User> findByUuid(final User.UserId userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
