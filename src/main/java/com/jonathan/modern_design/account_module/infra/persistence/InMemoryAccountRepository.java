package com.jonathan.modern_design.account_module.infra.persistence;

import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryAccountRepository implements AccountRepository {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findOne(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return Optional.ofNullable(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        List<Account> accountsList = new ArrayList<>(accounts.values());
        return new PageImpl<>(accountsList, pageable, accountsList.size());
    }

    @Override
    public Account create(Account account) {
        accounts.put(account.getAccountNumber().getAccountNumber(), account);
        return account;
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
    }

    public void delete(final String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        accounts.remove(accountNumber);
    }

}
