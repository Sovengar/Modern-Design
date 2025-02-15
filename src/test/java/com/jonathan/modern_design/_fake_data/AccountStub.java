package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.UserStub.normalUser;

public class AccountStub extends Stub {

    public static String sourceAccountId = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static String targetAccountId = "0db3c62f-c978-41ad-95a9-9230aa85593f";

    public static Account sourceAccountwithBalance(double balance) {
        return builder(sourceAccountId, balance, true);
    }

    public static Account sourceAccountEmpty() {
        return builder(sourceAccountId, 0.0, true);
    }

    public static Account sourceAccountInactive() {
        return builder(sourceAccountId, 0.0, false);
    }

    public static Account targetAccountwithBalance(double balance) {
        return builder(targetAccountId, balance, true);
    }

    public static Account targetAccountEmpty() {
        return builder(targetAccountId, 0.0, true);
    }

    public static Account targetAccountInactive() {
        return builder(targetAccountId, 0.0, false);
    }

    public static Account targetAccountWithDifferentCurrency() {
        return builder(targetAccountId, 0.0, true, Currency.BRITISH_POUND);
    }

    private static Account builder(String accountId, double balance, boolean isActive) {
        return builder(accountId, balance, isActive, Currency.EURO);
    }

    private static Account builder(String accountId, double balance, boolean isActive, Currency currency) {
        return Account.builder()
                .accountNumber(accountId)
                .money(AccountMoney.of(BigDecimal.valueOf(balance), currency))
                .address(AccountAddress.of("street", "city", "state", "zipCode"))
                .user(normalUser())
                .active(isActive).build();
    }
}
