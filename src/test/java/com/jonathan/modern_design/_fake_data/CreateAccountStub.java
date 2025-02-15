package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.account_module.application.create_account.CreateAccountCommand;
import com.jonathan.modern_design.shared.Currency;

public class CreateAccountStub {

    public static CreateAccountCommand randomAccountWithCurrency(Currency currency) {
        return CreateAccountCommand.builder()
                .username("Account Name")
                .email("z3u1E@example.com")
                .realname("John Doe")
                .address("street, city, state, zipCode")
                .currency(currency)
                .password("123456")
                .country("FR")
                .build();
    }
}
