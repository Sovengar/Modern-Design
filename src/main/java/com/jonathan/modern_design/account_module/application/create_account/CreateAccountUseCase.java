package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.account_module.domain.model.Account;

public interface CreateAccountUseCase {
    Account createAccount(final CreateAccountCommand command);
}
