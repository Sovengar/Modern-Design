package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.user_module.domain.model.User;

import java.math.BigDecimal;

public class AccountStub {
    public static Account withBalance(long accountId, double balance) {
        return Account.create(accountId, BigDecimal.valueOf(balance), Currency.EURO, User.builder().name("normal user").build());
    }

    public static Account emptyAccount(long accountId) {
        return withBalance(accountId, 0.0);
    }
}
