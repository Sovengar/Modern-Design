package jonathan.modern_design.user.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.domain.catalogs.Roles;
import jonathan.modern_design.user.domain.repos.RoleRepo;
import jonathan.modern_design.user.domain.repos.UserRepo;
import jonathan.modern_design.user.domain.vo.UserEmail;
import jonathan.modern_design.user.domain.vo.UserPassword;
import jonathan.modern_design.user.domain.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.vo.UserRealName;
import jonathan.modern_design.user.domain.vo.UserUserName;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;

import static java.lang.String.format;

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
@Transactional
public class RegisterUser {
    private final UserRepo repository;
    private final RoleRepo roleRepo;

    public void handle(final @Valid UserRegisterCommand command) {
        log.info("BEGIN RegisterUser");

        repository.findByUuid(new UserId(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin of Complex logic to know the final role of the user
        var roleCode = new Role.Code(Roles.USER.code());
        var role = roleRepo.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the user
        var user = User.register(new UserId(command.uuid()), UserRealName.of(command.realname().orElse("")), UserUserName.of(command.username()), UserEmail.of(command.email()), UserPassword.of(command.password()), command.country(), UserPhoneNumbers.of(command.phoneNumbers()), role);
        repository.registerUser(user);

        log.info("END RegisterUser");
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}

