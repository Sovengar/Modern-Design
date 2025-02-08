package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;

import static com.jonathan.modern_design.fake_data.UserStub.DEFAULT_COUNTRY;

public class CreateUserMother extends Stub {
    public static CreateUserCommand createUserCommandWithValidData() {
        return CreateUserCommand.builder()
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(8, 16))
                .country(DEFAULT_COUNTRY)
                .build();
    }

    public static CreateUserCommand createUserCommandWithShortPassword() {
        return CreateUserCommand.builder()
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(3, 6))
                .country(DEFAULT_COUNTRY)
                .build();
    }

    public static CreateUserCommand createUserCommandWithTooLongPassword() {
        return CreateUserCommand.builder()
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(55, 55))
                .country(DEFAULT_COUNTRY)
                .build();
    }
}
