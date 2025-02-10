package com.jonathan.modern_design.account_module.domain;

import com.jonathan.modern_design.account_module.domain.exceptions.AccountNotFoundException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findOne(final String accountNumber);

    Page<Account> findAll(final Pageable pageable);

    Account create(Account account);

    void update(Account account);

    void delete(final String accountNumber);

    void softDelete(final String accountNumber);

    void deposit(final String accountNumber, final BigDecimal amount, final Currency currency);

    default Account findOneOrElseThrow(final String accountNumber) {
        return findOne(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
