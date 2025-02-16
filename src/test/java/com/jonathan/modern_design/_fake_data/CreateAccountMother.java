package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.services.CreateAccountUseCase;

import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;

public class CreateAccountMother extends Stub {

    public static CreateAccountUseCase.CreateAccountCommand createAccountCommandWithInvalidData() {
        return CreateAccountUseCase.CreateAccountCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .currency(Currency.EURO.getCode())
                .password("123456")
                .country("XXX")
                .build();
    }

    public static CreateAccountUseCase.CreateAccountCommand createAccountCommandWithValidData() {
        return CreateAccountUseCase.CreateAccountCommand.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .realname(faker.name().fullName())
                .address("street, city, state, zipCode")
                .currency(Currency.EURO.getCode())
                .password("12345678")
                .country(DEFAULT_COUNTRY)
                .build();
    }
}
