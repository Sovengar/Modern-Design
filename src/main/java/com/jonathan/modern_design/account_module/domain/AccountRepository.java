package com.jonathan.modern_design.account_module.domain;

import com.jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.common.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findOne(final UUID accountId);

    Page<Account> findAll(final Pageable pageable);

    Account create(Account account);

    void update(Account account);

    void delete(final UUID accountId);

    void softDelete(final UUID accountId);

    void deposit(final UUID accountId, final BigDecimal amount, final Currency currency);

    default Account findOneOrElseThrow(final UUID accountId) {
        return findOne(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
