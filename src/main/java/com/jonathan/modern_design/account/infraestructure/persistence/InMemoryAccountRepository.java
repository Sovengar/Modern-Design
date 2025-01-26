package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.application.find_account.FindAccountUseCase;
import com.jonathan.modern_design.account.application.update_account.UpdateAccountUseCase;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.domain.exceptions.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryAccountRepository implements AccountRepository, FindAccountUseCase, UpdateAccountUseCase {
    private ConcurrentHashMap<String, Account> map = new ConcurrentHashMap<>();

    public Account save(Account account) {
        requireNonNull(account);
        map.put(account.dto().getId(), account);
        return account;
    }

    public Optional<Account> find(Long id) {
        Account account = map.get(id);
        return Optional.ofNullable(account);
    }

    public void delete(Long id) {
        map.remove(id);
    }

    public Page<Account> findAll(Pageable pageable) {
        List<Account> films = new ArrayList<>(map.values());
        return new PageImpl<>(films, pageable, films.size());
    }

    @Override
    public void update(Account account) {

    }
}
