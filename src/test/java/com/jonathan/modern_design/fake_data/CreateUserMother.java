package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.user_module.dtos.CreateUserCommand;

public class CreateUserMother extends Stub {
    public static final String DEFAULT_PASSWORD = "123456";
    public static final String DEFAULT_COUNTRY = "ES";

    public static CreateUserCommand normalUser() {
        return new CreateUserCommand(
                faker.name().name(),
                faker.internet().emailAddress(),
                faker.name().firstName(),
                faker.name().lastName(),
                DEFAULT_PASSWORD,
                DEFAULT_COUNTRY
        );
    }
}
