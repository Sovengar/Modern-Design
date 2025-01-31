package com.jonathan.modern_design.account_module.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringAccountRepository  extends JpaRepository<AccountEntity, UUID>  { }
