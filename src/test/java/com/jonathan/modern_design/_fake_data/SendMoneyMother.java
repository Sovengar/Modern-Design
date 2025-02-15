package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.account_module.application.transfer_money.TransferMoneyCommand;
import com.jonathan.modern_design.shared.Currency;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.sourceAccountId;
import static com.jonathan.modern_design._fake_data.AccountStub.targetAccountId;

public class SendMoneyMother extends Stub {
    private static TransferMoneyCommand fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, double amount) {
        return new TransferMoneyCommand(sourceAccountId, targetAccountId, BigDecimal.valueOf(amount), Currency.EURO);
    }

    public static TransferMoneyCommand transactionWithAmount(double amount) {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, amount);
    }

    public static TransferMoneyCommand insufficientFundsTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, 1000);
    }

    public static TransferMoneyCommand negativeAmountTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, -100);
    }
}
