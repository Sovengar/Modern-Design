package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.account_module.application.create_account.CreateAccountCommand;

public class AccountDataStub {

    public static CreateAccountCommand randomAccount() {
        return CreateAccountCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .currency("EUR")
                .password("123456")
                .country("FR")
                .build();
    }
}
