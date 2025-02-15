package com.jonathan.modern_design.account_module.application.transfer_money;

import lombok.NonNull;

public interface TransferMoneyUseCase {
    void transferMoney(@NonNull final TransferMoneyCommand command);
}
