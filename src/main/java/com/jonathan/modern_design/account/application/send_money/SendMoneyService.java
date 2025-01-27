package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.common.UseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {
    private final FindAccountUseCase findAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;

    @Transactional
    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        Account source = findAccountUseCase.findOne(command.sourceId())
                .orElseThrow(() -> new AccountNotFoundException(command.sourceId()));

        Account target = findAccountUseCase.findOne(command.targetId())
                .orElseThrow(() -> new AccountNotFoundException(command.targetId()));

        source.subtract(command.amount());
        target.add(command.amount());

        updateAccountUseCase.update(source);
        updateAccountUseCase.update(target);

        return true;
    }
}
