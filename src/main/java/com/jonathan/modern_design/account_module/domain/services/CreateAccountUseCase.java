package com.jonathan.modern_design.account_module.domain.services;

import com.jonathan.modern_design.account_module.domain.model.Account;
import lombok.Builder;

public interface CreateAccountUseCase {
    Account createAccount(final CreateAccountCommand command);

    @Builder
    record CreateAccountCommand(String realname, String email, String username, String address, String password, String country,
                                String currency) {
    }
}
