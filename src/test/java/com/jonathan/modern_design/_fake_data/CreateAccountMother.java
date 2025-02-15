package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.account_module.application.create_account.CreateAccountCommand;

public class CreateAccountMother {

    public static CreateAccountCommand createAccountCommandWithInvalidData() {
        return CreateAccountCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .currency("EUR")
                .password("123456")
                .country("FR")
                .build();
    }

    public static CreateAccountCommand createAccountCommandWithValidData() {
        return CreateAccountCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .address("street, city, state, zipCode")
                .currency("EUR")
                .password("123456")
                .country("FR")
                .build();
    }
}
