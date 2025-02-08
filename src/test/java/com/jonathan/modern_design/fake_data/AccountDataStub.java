package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account_module.application.create_account.AccountDataCommand;

public class AccountDataStub {

    public static AccountDataCommand randomAccount() {
        return AccountDataCommand.builder()
                .name("Account Name")
                .email("z3u1E@example.com")
                .firstname("John")
                .lastname("Doe")
                .password("123456")
                .country("FR")
                .build();
    }
}
