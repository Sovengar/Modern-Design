package com.jonathan.modern_design.account.application.send_money;

public interface SendMoneyUseCase {
    public boolean send(SendMoneyCommand command);
}
