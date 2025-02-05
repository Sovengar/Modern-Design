package com.jonathan.modern_design.account_module.application.create_account;

import com.jonathan.modern_design.account_module.domain.model.Account;
import lombok.NonNull;

public interface CreateAccountUseCase {
    Account createAccount(final AccountDataCommand command);
}
