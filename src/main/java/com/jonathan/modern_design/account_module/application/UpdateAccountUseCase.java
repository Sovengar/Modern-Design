package com.jonathan.modern_design.account_module.application;

public interface UpdateAccountUseCase {

    void update(FindAccountUseCase.AccountResource accountResource);
}
