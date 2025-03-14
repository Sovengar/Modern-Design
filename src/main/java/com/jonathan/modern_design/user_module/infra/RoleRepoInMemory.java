package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design.user_module.domain.Role;
import com.jonathan.modern_design.user_module.domain.RoleRepo;
import com.jonathan.modern_design.user_module.domain.Roles;

import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
class RoleRepoInMemory implements RoleRepo {
    private final ConcurrentHashMap<Role.Code, Role> codes = new ConcurrentHashMap<>();

    public RoleRepoInMemory() {
        codes.put(new Role.Code(Roles.ADMIN.getCode()), Role.of(Roles.ADMIN));
        codes.put(new Role.Code(Roles.USER.getCode()), Role.of(Roles.USER));
        codes.put(new Role.Code(Roles.TECHNICIAN.getCode()), Role.of(Roles.TECHNICIAN));
    }

    @Override
    public Role findByCode(final Role.Code code) {
        return codes.get(code);
    }
}
