package com.jonathan.modern_design.user_module.role;

public interface RoleRepo {
    Role findByCode(Role.Code code);

    Role findByCode(String desc);
}
