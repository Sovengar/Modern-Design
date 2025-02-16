package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.user_module.domain.services.RegisterUserUseCase;

import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;
import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_UUID;

public class CreateUserMother extends Stub {
    public static RegisterUserUseCase.RegisterUserCommand createUserCommandWithValidData() {
        return RegisterUserUseCase.RegisterUserCommand.builder()
                .uuid(DEFAULT_UUID)
                .realname(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password(faker.internet().password(8, 16))
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
