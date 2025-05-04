package jonathan.modern_design.user.application;

import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@WebAdapter("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
class DeleteUserHttpController {
    private final DeleteUser deleteUser;

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("BEGIN Controller - deleteUser");
        Assert.notNull(userId, "User id must not be null");
        deleteUser.handle(userId);
        log.info("END Controller - deleteUser");

        return ResponseEntity.ok().build();
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
class DeleteUser {
    private final UserRepo userRepo;

    public void handle(UUID userId) {
        log.info("BEGIN - deleteUser");
        userRepo.delete(User.Id.of(userId));
        log.info("END - deleteUser");
    }
}
