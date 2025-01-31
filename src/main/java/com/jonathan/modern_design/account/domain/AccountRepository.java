package com.jonathan.modern_design.account.domain;

import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.common.Currency;
import lombok.NonNull;
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
}
