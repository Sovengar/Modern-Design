package com.jonathan.modern_design.account.application.send_money;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
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
    public boolean send(SendMoneyCommand command) {

        Account source = findAccountUseCase.find(command.sourceId());
        Account target = findAccountUseCase.find(command.targetId());

        source.subtract(command.amount());
        target.add(command.amount());

        updateAccountUseCase.update(source);
        updateAccountUseCase.update(target);

        return true;
    }
}
