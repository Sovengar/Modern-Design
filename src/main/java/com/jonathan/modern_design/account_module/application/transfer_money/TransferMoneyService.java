package com.jonathan.modern_design.account_module.application.transfer_money;

import com.jonathan.modern_design.account_module.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account_module.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.services.AccountValidator;
import com.jonathan.modern_design.config.annotations.DomainService;
import com.jonathan.modern_design.shared.Currency;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@DomainService
@RequiredArgsConstructor
public class TransferMoneyService implements TransferMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountValidator accountValidator;

    @Override
    public void transferMoney(@NonNull final TransferMoneyCommand command) {

        Account source = getAccountValidated(command.sourceId());
        Account target = getAccountValidated(command.targetId());

        validateDifferentAccounts(source, target);

        final var amount = command.amount();
        final var currency = command.currency();

        transferMoney(source, target, amount, currency);
    }

    private Account getAccountValidated(String accountNumber) {
        var account = findAccountUseCase.findOne(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        accountValidator.validateAccount(account);
        return account;
    }

    private void validateDifferentAccounts(Account source, Account target) {
        var isSameAccount = source.getAccountNumber().equals(target.getAccountNumber());

        if (isSameAccount) {
            throw new OperationForbiddenForSameAccount();
        }
    }

    private void transferMoney(Account source, Account target, BigDecimal amount, Currency currency) {
        source.substract(amount, currency);
        target.deposit(amount, currency);

        updateAccountUseCase.update(source);
        updateAccountUseCase.update(target);
    }

}
