package com.jonathan.modern_design.user_module.domain;

public interface RoleRepo {
    Role findByCode(Role.Code code);
}
