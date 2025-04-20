package jonathan.modern_design.user.infra;

import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface UserRepoSpringDataJPA extends JpaRepository<User, Long> {
    Optional<User> findByUuid(User.UserId userId);
}

interface UserRepoSpringDataJDBC extends CrudRepository<User, Long> {
    Optional<User> findByUuid(User.UserId userId);
}

@Query
@RequiredArgsConstructor
class UserRepoAdapter implements UserRepo {
    private final UserRepoSpringDataJPA repository;
    private final UserRepoSpringDataJDBC repositoryJDBC;

    @Override
    public void registerUser(User user) {
        repository.save(user);
    }

    @Override
    public Optional<User> findByUuid(final User.UserId userId) {
        var aux = repositoryJDBC.findByUuid(userId);
        return repository.findByUuid(userId);
    }
}
