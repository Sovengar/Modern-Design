package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.services.AccountValidator;
import com.jonathan.modern_design.account.domain.exceptions.InsufficientFundsException;
import com.jonathan.modern_design.account.domain.exceptions.OperationForbiddenForSameAccount;
import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.common.Currency;
import com.jonathan.modern_design.common.UseCase;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountValidator accountValidator;

    @Transactional
    @Override
    public void sendMoney(@NonNull final SendMoneyCommand command) {

        Account source = getAccountValidated(command.sourceId());
        Account target = getAccountValidated(command.targetId());

        validateDifferentAccounts(source, target);

        final var amount = command.amount();
        final var currency = command.currency();

        if(!source.isBalanceGreaterThan(amount)) {
            throw new InsufficientFundsException();
        }

        transferMoney(source, target, amount, currency);
    }

    private Account getAccountValidated(UUID accountId){
         var account = findAccountUseCase.findOne(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        accountValidator.validateAccount(account);
        return account;
    }

    private void validateDifferentAccounts(Account source, Account target){
        var isSameAccount = source.getId().equals(target.getId());

        if(isSameAccount){
            throw new OperationForbiddenForSameAccount();
        }
    }

    private void transferMoney(Account source, Account target, BigDecimal amount, Currency currency){
        source.substract(amount, currency);
        target.add(amount, currency);

        updateAccountUseCase.update(source);
        updateAccountUseCase.update(target);
    }

}
