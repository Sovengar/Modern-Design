package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.user_module.domain.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountStub {

    public static UUID sourceAccountId = UUID.randomUUID();
    public static UUID targetAccountId = UUID.randomUUID();

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

    private static Account builder(UUID accountId, double balance, boolean isActive){
        return Account.builder()
                .uuid(accountId)
                .money(AccountMoneyVO.of(BigDecimal.valueOf(balance), Currency.EURO))
                .user(User.builder().name("normal user").build())
                .isActive(isActive).build();
    }
}
