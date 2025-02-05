package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account_module.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.common.Currency;

import java.math.BigDecimal;
import java.util.UUID;

import static com.jonathan.modern_design.fake_data.AccountStub.sourceAccountId;
import static com.jonathan.modern_design.fake_data.AccountStub.targetAccountId;

public class SendMoneyMother extends Stub {
    public static SendMoneyCommand fromAccountToAccountWithAmount(UUID sourceAccountId, UUID targetAccountId, double amount) {
        return new SendMoneyCommand(sourceAccountId, targetAccountId, BigDecimal.valueOf(amount), Currency.EURO);
    }

    public static SendMoneyCommand transactionWithAmount(double amount) {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, amount);
    }

    public static SendMoneyCommand insufficientFundsTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, 1000);
    }

    public static SendMoneyCommand negativeAmountTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, -100);
    }
}
