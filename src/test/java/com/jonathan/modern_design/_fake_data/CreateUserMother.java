package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.user_module.application.RegisterUserCommand;

import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;
import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_UUID;

public class CreateUserMother extends Stub {
    public static RegisterUserCommand createUserCommandWithValidData() {
        return RegisterUserCommand.builder()
                .uuid(DEFAULT_UUID)
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(8, 16))
                .country(DEFAULT_COUNTRY)
                .build();
    }

    public static RegisterUserCommand createUserCommandWithShortPassword() {
        return RegisterUserCommand.builder()
                .uuid(DEFAULT_UUID)
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(3, 6))
                .country(DEFAULT_COUNTRY)
                .build();
    }

    public static RegisterUserCommand createUserCommandWithTooLongPassword() {
        return RegisterUserCommand.builder()
                .uuid(DEFAULT_UUID)
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(55, 55))
                .country(DEFAULT_COUNTRY)
                .build();
    }
}
