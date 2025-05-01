package jonathan.modern_design.user.domain.store;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design.user.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    void registerUser(User user);

    void updateUser(User user);

    Optional<User> findByUuid(User.Id userId);

    List<User> findAll();

    default User findByUUIDOrElseThrow(User.Id userId) {
        return findByUuid(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
