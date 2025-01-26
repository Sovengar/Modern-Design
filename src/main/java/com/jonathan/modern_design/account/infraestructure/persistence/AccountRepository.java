package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> find(Long id);
    void delete(Long id);
    Page<Account> findAll(Pageable pageable);
}
