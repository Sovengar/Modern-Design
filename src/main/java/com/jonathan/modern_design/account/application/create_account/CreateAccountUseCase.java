package com.jonathan.modern_design.account.application.create_account;

import com.jonathan.modern_design.account.domain.model.Account;
import lombok.NonNull;

public interface CreateAccountUseCase {
    Account createAccount(@NonNull AccountDataCommand command);
}
