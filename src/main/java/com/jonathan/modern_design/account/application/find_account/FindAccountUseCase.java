package com.jonathan.modern_design.account.application.find_account;

import com.jonathan.modern_design.account.domain.model.Account;

import java.util.Optional;

public interface FindAccountUseCase {
    Optional<Account> findOne(Long id);
}
