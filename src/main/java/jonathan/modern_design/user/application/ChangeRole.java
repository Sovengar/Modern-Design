package jonathan.modern_design.user.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.user.domain.models.Role;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.store.RoleStore;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/roles")
class ChangeRoleHttpController {
    private final ChangeRole changeRole;

    @PutMapping(value = "/{userId}/changeRoleTo/{roleCode}")
    public ResponseEntity<Void> changeRole(final User.Id userId, final Role.Code roleCode) {
        log.info("BEGIN Controller - ChangeRole");
        var message = new ChangeRole.Command(userId, roleCode);
        changeRole.handle(message);
        log.info("END Controller - ChangeRole");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class ChangeRole {
    private final UserRepo userRepo;
    private final RoleStore roleStore;

    //TODO GET THE USER THAT TRIGGERED THE COMMAND AND VALIDATE IF IT CAN CHANGE ROLES OF OTHER USERS

    //If logic grows complex move to a domain service
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
