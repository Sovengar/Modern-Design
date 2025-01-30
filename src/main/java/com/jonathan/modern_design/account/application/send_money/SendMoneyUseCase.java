package com.jonathan.modern_design.account.application.send_money;

import lombok.NonNull;

public interface SendMoneyUseCase {
    void sendMoney(@NonNull final SendMoneyCommand command);
}
