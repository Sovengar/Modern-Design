package jonathan.modern_design.user.domain.repos;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design.user.domain.User;

import java.util.Optional;

public interface FindUserRepo {
    Optional<User> findByUuid(User.UserId userId);

    default User findByUUIDOrElseThrow(User.UserId userId) {
        return findByUuid(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
