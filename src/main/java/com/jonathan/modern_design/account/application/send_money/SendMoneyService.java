package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.AccountValidator;
import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.common.UseCase;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final AccountValidator accountValidator;

    @Transactional
    @Override
    public void sendMoney(@NonNull final SendMoneyCommand command) {
        Account source = findAccountUseCase.findOne(command.sourceId())
                .orElseThrow(() -> new AccountNotFoundException(command.sourceId()));

        accountValidator.validateAccount(source);

        Account target = findAccountUseCase.findOne(command.targetId())
                .orElseThrow(() -> new AccountNotFoundException(command.targetId()));

        accountValidator.validateAccount(target);

        final var amount = command.amount();
        final var currency = command.currency();

        source.substract(amount, currency);
        target.add(amount, currency);

        updateAccountUseCase.update(source);
        updateAccountUseCase.update(target);
    }
}
