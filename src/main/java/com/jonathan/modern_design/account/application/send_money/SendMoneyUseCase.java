package com.jonathan.modern_design.account.application.send_money;

public interface SendMoneyUseCase {
    boolean sendMoney(SendMoneyCommand command);
}
