package jonathan.modern_design.auth.application;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.events.banking.AccountHolderDeleted;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;

@WebAdapter("/v1/users")
@Slf4j
@RequiredArgsConstructor
class DeleteUserHttpController {
    private final DeleteUser deleteUser;

    @Operation(summary = "Delete a auth")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable UUID userId) {
        Assert.notNull(userId, "User id must not be null");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN deleteUser for userId: {}", userId);
        deleteUser.handle(User.Id.of(userId));
        log.info("END deleteUser for userId: {}", userId);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
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

    //Sync deletion, move to async if required with according compensations in case of async error...
    @EventListener
    void handle(AccountHolderDeleted event) {
        handle(User.Id.of(event.userId()));
    }
}
