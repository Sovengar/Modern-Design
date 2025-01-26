package com.jonathan.modern_design.account.application.find_account;

import com.jonathan.modern_design.account.domain.Account;

import java.util.Optional;

public interface FindAccountUseCase {
    Optional<Account> find(Long id);
}
