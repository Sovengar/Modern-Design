package jonathan.modern_design.auth.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.store.RoleStore;
import jonathan.modern_design.auth.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/roles")
class ChangeRoleHttpController {
    private final ChangeRole changeRole;

    @Operation(summary = "Change the role of a auth")
    @PutMapping(value = "/{userId}/changeRoleTo/{roleCode}")
    public ResponseEntity<Response<Void>> changeRole(final @PathVariable UUID userId, @PathVariable final String roleCode) {
        generateTraceId();
        //Authentication + Authorization

        var message = new ChangeRole.Command(User.Id.of(userId), Role.Code.of(roleCode));

        log.info("BEGIN ChangeRole for userId: {} with role: {}", userId, roleCode);
        changeRole.handle(message);
        log.info("END ChangeRole for userId: {}, with role: {}", userId, roleCode);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class ChangeRole {
    private final UserRepo userRepo;
    private final RoleStore roleStore;

    //If logic grows complex, move to a domain service
    public void handle(final @Valid ChangeRole.Command message) {
        log.info("BEGIN - ChangeRole");
        var user = userRepo.findByUUIDOrElseThrow(message.userId());
        Role newRole = roleStore.findByCode(message.roleCode());
        user.changeRole(newRole);
        userRepo.updateUser(user);
        log.info("END - ChangeRole");
    }

    record Command(User.Id userId, Role.Code roleCode) {
    }
}
