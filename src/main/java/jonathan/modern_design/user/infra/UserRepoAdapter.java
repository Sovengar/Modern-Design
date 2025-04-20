package jonathan.modern_design.user.infra;

import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepoSpringDataJPA extends JpaRepository<User, Long> {
    Optional<User> findByUuid(User.UserId userId);
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
    public Optional<User> findByUuid(final User.UserId userId) {
        return repository.findByUuid(userId);
    }
}
