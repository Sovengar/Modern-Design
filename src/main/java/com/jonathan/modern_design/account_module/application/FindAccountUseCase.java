package com.jonathan.modern_design.account_module.application;

public interface FindAccountUseCase {
    AccountResource findOne(final String accountNumber);
}
