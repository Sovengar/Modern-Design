package com.jonathan.modern_design.user_module.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleSpringRepo extends JpaRepository<Role, Role.Code> {
    Role findByDescription(String desc);
}
