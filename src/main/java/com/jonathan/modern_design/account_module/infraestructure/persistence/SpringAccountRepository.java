package com.jonathan.modern_design.account_module.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringAccountRepository extends JpaRepository<AccountEntity, String> {
}
