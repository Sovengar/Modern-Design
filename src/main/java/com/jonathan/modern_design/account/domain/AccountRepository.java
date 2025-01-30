package com.jonathan.modern_design.account.domain;

import com.jonathan.modern_design.account.domain.model.Account;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findOne(@NonNull Long id);
    Page<Account> findAll(Pageable pageable);
    Account create(@NonNull Account account);
    void update(@NonNull Account account);
    void delete(@NonNull final Long id);
    void softDelete(@NonNull final Long id);
}
