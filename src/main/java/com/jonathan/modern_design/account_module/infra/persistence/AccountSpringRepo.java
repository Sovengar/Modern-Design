package com.jonathan.modern_design.account_module.infra.persistence;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSpringRepo extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}
