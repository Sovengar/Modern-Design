package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryAccountRepository implements AccountRepository {
    private final ConcurrentHashMap<Long, Account> data = new ConcurrentHashMap<>();

    public Optional<Account> findOne(Long id) {
        Account account = data.get(id);
        return Optional.ofNullable(account);
    }

    public Page<Account> findAll(Pageable pageable) {
        List<Account> accounts = new ArrayList<>(data.values());
        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public Account create(Account account) {
        requireNonNull(account);
        data.put(account.getId(), account);
        return account;
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
    }

    public void delete(Long id) {
        data.remove(id);
    }
}
