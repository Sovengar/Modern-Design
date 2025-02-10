package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;
import java.util.UUID;

import static com.jonathan.modern_design.fake_data.UserStub.normalUser;

public class AccountStub extends Stub {

    public static String sourceAccountId = UUID.randomUUID().toString();
    public static String targetAccountId = UUID.randomUUID().toString();

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
                .money(AccountMoneyVO.of(BigDecimal.valueOf(balance), currency))
                .user(normalUser())
                .active(isActive).build();
    }
}
