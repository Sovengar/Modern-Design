package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account.domain.Account;

import java.math.BigDecimal;

public class AccountStub {
    public static Account withBalance(long accountId, double balance) {
        return Account.create(accountId, BigDecimal.valueOf(balance));
    }

    public static Account emptyAccount(long accountId) {
        return withBalance(accountId, 0.0);
    }
}
