package jonathan.modern_design.user.domain.store;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design.user.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    void registerUser(User user);

    void updateUser(User user);

    Optional<User> findById(User.Id userId);

    List<User> findAll();

    void delete(User.Id userId);

    default User findByUUIDOrElseThrow(User.Id userId) {
        return findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
