package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.account.domain.model.Account;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryAccountRepository implements AccountRepository {
    private final ConcurrentHashMap<Long, Account> accounts = new ConcurrentHashMap<>();

    public Optional<Account> findOne(@NonNull Long id) {
        Account account = accounts.get(id);
        return Optional.ofNullable(account);
    }

    public Page<Account> findAll(Pageable pageable) {
        List<Account> accountsList = new ArrayList<>(accounts.values());
        return new PageImpl<>(accountsList, pageable, accountsList.size());
    }

    @Override
    public Account create(@NonNull Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public void update(@NonNull Account account) {
        requireNonNull(account);
    }

    public void delete(@NonNull final Long id) {
        accounts.remove(id);
    }

    @Override
    public void softDelete(@NonNull Long id) {
        accounts.remove(id);
    }
}
