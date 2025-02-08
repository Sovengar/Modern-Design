package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account_module.application.create_account.AccountDataCommand;

public class AccountDataStub {

    public static AccountDataCommand randomAccount() {
        return AccountDataCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .currency("EUR")
                .password("123456")
                .country("FR")
                .build();
    }
}
