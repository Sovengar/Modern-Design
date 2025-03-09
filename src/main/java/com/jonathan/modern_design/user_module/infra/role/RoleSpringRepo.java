package com.jonathan.modern_design.user_module.infra.role;

import com.jonathan.modern_design.user_module.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleSpringRepo extends JpaRepository<Role, Role.Code> {
}
