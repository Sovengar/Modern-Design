package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.__config.Stub;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.model.UserEmail;
import com.jonathan.modern_design.user_module.domain.model.UserName;
import com.jonathan.modern_design.user_module.domain.model.UserPassword;
import com.jonathan.modern_design.user_module.domain.model.UserRealName;
import com.jonathan.modern_design.user_module.domain.services.RegisterUserUseCase;

import java.util.UUID;

public class UserStub extends Stub {
    public static final String DEFAULT_COUNTRY = "ES";
    public static final UUID DEFAULT_UUID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");

    public static final String VALID_PASSWORD = faker.internet().password(4, 12, true, true, true) + "1ºAa";

    public static User normalUser() {
        return User.builder()
                .uuid(DEFAULT_UUID)
                .realname(UserRealName.of(faker.name().fullName()))
                .username(UserName.of(faker.name().username()))
                .email(UserEmail.of(faker.internet().emailAddress()))
                .password(UserPassword.of(VALID_PASSWORD))
                .country(DEFAULT_COUNTRY)
                .build();
    }

    public static class CreateUserMother extends Stub {
        public static RegisterUserUseCase.RegisterUserCommand createUserCommandWithValidData() {
            return RegisterUserUseCase.RegisterUserCommand.builder()
                    .uuid(DEFAULT_UUID)
                    .realname(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .username(faker.name().username())
                    .password(VALID_PASSWORD)
                    .country(DEFAULT_COUNTRY)
                    .build();
        }

        public static RegisterUserUseCase.RegisterUserCommand createUserCommandWithShortPassword() {
            return RegisterUserUseCase.RegisterUserCommand.builder()
                    .uuid(DEFAULT_UUID)
                    .realname(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .username(faker.name().username())
                    .password(faker.internet().password(3, 6))
                    .country(DEFAULT_COUNTRY)
                    .build();
        }

        public static RegisterUserUseCase.RegisterUserCommand createUserCommandWithTooLongPassword() {
            return RegisterUserUseCase.RegisterUserCommand.builder()
                    .uuid(DEFAULT_UUID)
                    .realname(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .username(faker.name().username())
                    .password(faker.internet().password(55, 55))
                    .country(DEFAULT_COUNTRY)
                    .build();
        }
    }
}
