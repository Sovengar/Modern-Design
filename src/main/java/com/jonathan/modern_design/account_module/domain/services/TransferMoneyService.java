package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design._infra.config.annotations.DomainService;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@DomainService
@RequiredArgsConstructor
public class TransferMoneyService implements TransferMoneyUseCase {
    private final AccountRepository repository;
    private final AccountValidator accountValidator;

    @Override
    public void transferMoney(final TransferMoneyCommand command) {
        Account source = getAccountValidated(command.sourceId());
        Account target = getAccountValidated(command.targetId());

        validateDifferentAccounts(source.getAccountNumber(), target.getAccountNumber());

        final var amount = command.amount();
        final var currency = command.currency();

        transferMoney(source, target, amount, currency);
    }

    private Account getAccountValidated(final String accountNumber) {
        var account = repository.findOneOrElseThrow(accountNumber);
        accountValidator.validateAccount(account);
        return account;
    }

    private void validateDifferentAccounts(final AccountNumber source, final AccountNumber target) {
        var isSameAccount = source.equals(target);

        if (isSameAccount) {
            throw new OperationForbiddenForSameAccount();
        }
    }

    private void transferMoney(Account source, Account target, final BigDecimal amount, final Currency currency) {
        source.substract(amount, currency);
        target.add(amount, currency);

        repository.update(source);
        repository.update(target);
    }

}
