package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.services.TransferMoneyUseCase;

import java.math.BigDecimal;

import static com.jonathan.modern_design._fake_data.AccountStub.sourceAccountId;
import static com.jonathan.modern_design._fake_data.AccountStub.targetAccountId;

public class SendMoneyMother extends Stub {
    public static TransferMoneyUseCase.TransferMoneyCommand fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, double amount) {
        return new TransferMoneyUseCase.TransferMoneyCommand(sourceAccountId, targetAccountId, BigDecimal.valueOf(amount), Currency.EURO);
    }

    public static TransferMoneyUseCase.TransferMoneyCommand transactionWithAmount(double amount) {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, amount);
    }

    public static TransferMoneyUseCase.TransferMoneyCommand insufficientFundsTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, 1000);
    }

    public static TransferMoneyUseCase.TransferMoneyCommand negativeAmountTransaction() {
        return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, -100);
    }
}
