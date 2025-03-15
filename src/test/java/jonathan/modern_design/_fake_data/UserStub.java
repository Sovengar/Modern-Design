package jonathan.modern_design._fake_data;

import jonathan.modern_design.__config.Stub;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.user_module.domain.Role;
import jonathan.modern_design.user_module.domain.Roles;
import jonathan.modern_design.user_module.domain.User;
import jonathan.modern_design.user_module.domain.User.UserId;
import jonathan.modern_design.user_module.domain.vo.UserEmail;
import jonathan.modern_design.user_module.domain.vo.UserName;
import jonathan.modern_design.user_module.domain.vo.UserPassword;
import jonathan.modern_design.user_module.domain.vo.UserRealName;
import jonathan.modern_design.user_module.dtos.UserRegisterCommand;

import java.util.Optional;
import java.util.UUID;

public class UserStub extends Stub {
    public static final Country DEFAULT_COUNTRY = new Country("ES", "Spain");
    public static final UUID DEFAULT_UUID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");

    public static final String VALID_PASSWORD = faker.internet().password(4, 12, true, true, true) + "1ºAa";

    public static User normalUser() {
        return User.register(
                new UserId(DEFAULT_UUID),
                UserRealName.of(faker.name().fullName()),
                UserName.of(faker.name().username()),
                UserEmail.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD),
                DEFAULT_COUNTRY,
                Role.of(Roles.TECHNICIAN));
    }

    public static User adminUser() {
        return User.registerAdmin(
                new UserId(DEFAULT_UUID),
                UserRealName.of(faker.name().fullName()),
                UserName.of(faker.name().username()),
                UserEmail.of(faker.internet().emailAddress()),
                UserEmail.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD),
                DEFAULT_COUNTRY);
    }

    public static class CreateValidUser extends Stub {
        public static UserRegisterCommand createUserCommandWithValidData() {
            return new UserRegisterCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), VALID_PASSWORD, DEFAULT_COUNTRY);
        }
    }

    public static class CreateInvalidUser extends Stub {
        public static UserRegisterCommand createUserWithShortPassword() {
            return new UserRegisterCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(3, 6), DEFAULT_COUNTRY);
        }

        public static UserRegisterCommand createUserWithTooLongPassword() {
            return new UserRegisterCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(55, 55), DEFAULT_COUNTRY);
        }

        public static UserRegisterCommand createUserWithInvalidEmail() {
            return new UserRegisterCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), "invalid_email", VALID_PASSWORD, DEFAULT_COUNTRY);
        }
    }
}
