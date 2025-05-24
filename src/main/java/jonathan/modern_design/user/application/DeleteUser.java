package jonathan.modern_design.user.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
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

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@WebAdapter("/v1/users")
@Slf4j
@RequiredArgsConstructor
class DeleteUserHttpController {
    private final DeleteUser deleteUser;

    @Observed(name = "deleteUser")
    @Operation(summary = "Delete a user")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        generateTraceId();
        Assert.notNull(userId, "User id must not be null");

        log.info("BEGIN deleteUser for userId: {}", userId);
        deleteUser.handle(User.Id.of(userId));
        log.info("END deleteUser for userId: {}", userId);

        return ResponseEntity.ok().build();
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
class DeleteUser {
    private final UserRepo userRepo;

    public void handle(User.Id userId) {
        log.info("BEGIN - deleteUser");
        userRepo.delete(userId);
        log.info("END - deleteUser");
    }
}
