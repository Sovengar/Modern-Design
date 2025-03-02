package com.jonathan.modern_design.account_module.application;

import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import lombok.Builder;

public interface CreateAccountUseCase {
    AccountNumber createAccount(final CreateAccountCommand command);

    @Builder
    record CreateAccountCommand(String realname, String email, String username, String address, String password, String country,
                                String currency) {
    }
}
