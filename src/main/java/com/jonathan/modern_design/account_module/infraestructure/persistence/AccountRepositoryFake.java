package com.jonathan.modern_design.account_module.infraestructure.persistence;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.shared.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class AccountRepositoryFake implements AccountRepository {
    private final ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findOne(UUID id) {
        Account account = accounts.get(id);
        return Optional.ofNullable(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        List<Account> accountsList = new ArrayList<>(accounts.values());
        return new PageImpl<>(accountsList, pageable, accountsList.size());
    }

    @Override
    public Account create(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
    }

    public void delete(final UUID id) {
        accounts.remove(id);
    }

    @Override
    public void softDelete(final UUID id) {
        accounts.remove(id);
    }

    @Override
    public void deposit(UUID accountId, BigDecimal amount, Currency currency) {
        accounts.get(accountId).deposit(amount, currency);
    }
}
