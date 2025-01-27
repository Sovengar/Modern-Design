package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findOne(Long id);
    Page<Account> findAll(Pageable pageable);

    Account create(Account account);
    void update(Account account);
    void delete(Long id);
}
