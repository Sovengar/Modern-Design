package com.jonathan.modern_design.account.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringAccountRepository  extends JpaRepository<AccountEntity, Long>  { }
