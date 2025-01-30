package com.jonathan.modern_design.fake_data;

import com.jonathan.modern_design.account.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.common.Currency;

import java.math.BigDecimal;

public class SendMoneyCommandMother {

    public static SendMoneyCommand fromAccountToAccountWithAmount(
            long sourceAccountId, long targetAccountId, double amount) {
        return new SendMoneyCommand(sourceAccountId, targetAccountId, BigDecimal.valueOf(amount), Currency.EURO);
        //return SendMoneyCommand.builder().sourceId(sourceAccountId).targetId(targetAccountId).amount(BigDecimal.valueOf(amount)).build();
    }

    public static SendMoneyCommand transactionWithAmount(double amount) {
        return fromAccountToAccountWithAmount(1L, 2L, amount);
    }

    public static SendMoneyCommand insufficientFundsTransaction() {
        return fromAccountToAccountWithAmount(1L, 2L, 1000);
    }

    public static SendMoneyCommand negativeAmountTransaction() {
        return fromAccountToAccountWithAmount(1L, 2L, -100);
    }
}
