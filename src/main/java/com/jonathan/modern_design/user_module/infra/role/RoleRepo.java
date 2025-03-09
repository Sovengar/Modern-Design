package com.jonathan.modern_design.user_module.infra.role;

import com.jonathan.modern_design.user_module.domain.model.Role;

public interface RoleRepo {
    Role findByCode(Role.Code code);
}
