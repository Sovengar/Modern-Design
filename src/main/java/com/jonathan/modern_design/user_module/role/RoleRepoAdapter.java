package com.jonathan.modern_design.user_module.role;

import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RoleRepoAdapter implements RoleRepo {
    private final RoleSpringRepo repository;

    @Override
    public Role findByCode(Role.Code code) {
        return repository.findById(code).orElseThrow(EntityNotFoundException::new);
    }
}
