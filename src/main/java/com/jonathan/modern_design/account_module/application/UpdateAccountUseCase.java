package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.dtos.AccountResource;

public interface UpdateAccountUseCase {

    void update(AccountResource accountResource);
}
