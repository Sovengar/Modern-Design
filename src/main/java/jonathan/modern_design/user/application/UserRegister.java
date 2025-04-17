package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.RoleRepo;
import jonathan.modern_design.user.domain.Roles;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.domain.UserRepo;
import jonathan.modern_design.user.domain.vo.UserEmail;
import jonathan.modern_design.user.domain.vo.UserPassword;
import jonathan.modern_design.user.domain.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.vo.UserRealName;
import jonathan.modern_design.user.domain.vo.UserUserName;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

import static java.lang.String.format;

@Injectable
@RequiredArgsConstructor
public class UserRegister {
    private final UserRepo repository;
    private final RoleRepo roleRepo;

    public void registerUser(UserRegisterCommand command) {
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
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
