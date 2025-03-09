package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.__config.Stub;
import com.jonathan.modern_design._shared.country.Country;
import com.jonathan.modern_design.user_module.role.Role;
import com.jonathan.modern_design.user_module.role.Roles;
import com.jonathan.modern_design.user_module.user.application.RegisterUserUseCase;
import com.jonathan.modern_design.user_module.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public class UserStub extends Stub {
    public static final Country DEFAULT_COUNTRY = new Country("ES", "Spain");
    public static final UUID DEFAULT_UUID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");

    public static final String VALID_PASSWORD = faker.internet().password(4, 12, true, true, true) + "1ºAa";

    public static User normalUser() {
        return User.register(new User.ID(DEFAULT_UUID), faker.name().fullName(), faker.name().username(), faker.internet().emailAddress(), VALID_PASSWORD, DEFAULT_COUNTRY, Role.of(Roles.TECHNICIAN));
    }

    public static User adminUser() {
        return User.registerAdmin(new User.ID(DEFAULT_UUID), faker.name().fullName(), faker.name().username(), faker.internet().emailAddress(), faker.internet().emailAddress(), VALID_PASSWORD, DEFAULT_COUNTRY);
    }

    public static class CreateValidUser extends Stub {
        public static RegisterUserUseCase.RegisterUserCommand createUserCommandWithValidData() {
            return new RegisterUserUseCase.RegisterUserCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), VALID_PASSWORD, DEFAULT_COUNTRY);
        }
    }

    public static class CreateInvalidUser extends Stub {
        public static RegisterUserUseCase.RegisterUserCommand createUserWithShortPassword() {
            return new RegisterUserUseCase.RegisterUserCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(3, 6), DEFAULT_COUNTRY);
        }

        public static RegisterUserUseCase.RegisterUserCommand createUserWithTooLongPassword() {
            return new RegisterUserUseCase.RegisterUserCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(55, 55), DEFAULT_COUNTRY);
        }

        public static RegisterUserUseCase.RegisterUserCommand createUserWithInvalidEmail() {
            return new RegisterUserUseCase.RegisterUserCommand(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), "invalid_email", VALID_PASSWORD, DEFAULT_COUNTRY);
        }
    }
}
