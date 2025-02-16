package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.services.CreateAccountUseCase;

import static com.jonathan.modern_design._fake_data.Stub.faker;
import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;

public class CreateAccountStub {

    public static CreateAccountUseCase.CreateAccountCommand randomAccountWithCurrency(Currency currency) {
        return CreateAccountUseCase.CreateAccountCommand.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .realname(faker.name().fullName())
                .address("street, city, state, zipCode")
                .currency(currency.getCode())
                .password("12345678")
                .country(DEFAULT_COUNTRY)
                .build();
    }
}
