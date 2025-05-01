package jonathan.modern_design.user.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.user.domain.catalogs.Roles;
import jonathan.modern_design.user.domain.models.Role;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.models.vo.UserEmail;
import jonathan.modern_design.user.domain.models.vo.UserPassword;
import jonathan.modern_design.user.domain.models.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.models.vo.UserRealName;
import jonathan.modern_design.user.domain.models.vo.UserUserName;
import jonathan.modern_design.user.domain.store.RoleStore;
import jonathan.modern_design.user.domain.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Controller
@RequiredArgsConstructor
class RegisterUserGraphQLController {
    private final RegisterUser registerUser;
    private final UserRepo userRepo;

    @MutationMapping
    public User registerUser(@Argument RegisterUser.Command message) {
        /*
        User.Id uuid = User.Id.of(UUID.randomUUID());
        UserRealName realname = input.getRealname() != null ? UserRealName.of(input.getRealname()) : null;
        UserUserName username = UserUserName.of(input.getUsername());
        UserEmail email = UserEmail.of(input.getEmail());
        UserPassword password = UserPassword.of(input.getPassword());
        Country country = Country.of(input.getCountry());
        UserPhoneNumbers phoneNumbers = new UserPhoneNumbers(input.getPhoneNumbers());
*/
        //var message = new RegisterUser.Command(uuid, realname, username, email, password, country, phoneNumbers);

        var userId = registerUser.handle(message);
        return userRepo.findByUUIDOrElseThrow(User.Id.of(userId));
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
@Transactional
public class RegisterUser {
    private final UserRepo repository;
    private final RoleStore roleStore;

    public UUID handle(final @Valid RegisterUser.Command command) {
        log.info("BEGIN RegisterUser");

        repository.findByUuid(User.Id.of(command.uuid())).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.uuid()));
        });

        //Begin with Complex logic to know the final role of the user
        var roleCode = Role.Code.of(Roles.USER.code());
        var role = roleStore.findByCode(roleCode);
        //End of complex logic

        //Complex logic to decide the user
        var user = User.Factory.register(User.Id.of(command.uuid()), UserRealName.of(command.realname().orElse("")), UserUserName.of(command.username()), UserEmail.of(command.email()), UserPassword.of(command.password()), command.country(), UserPhoneNumbers.of(command.phoneNumbers()), role);
        repository.registerUser(user);

        log.info("END RegisterUser");
        return user.uuid().userUuid();
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public record Command(UUID uuid, Optional<String> realname, String username, String email, String password,
                          Country country, List<String> phoneNumbers) {
    }
}

