package com.jonathan.modern_design.account_module.application.find_account;

import com.jonathan.modern_design.account_module.domain.model.Account;

import java.util.Optional;

public interface FindAccountUseCase {
    Optional<Account> findOne(String accountNumber);
}
