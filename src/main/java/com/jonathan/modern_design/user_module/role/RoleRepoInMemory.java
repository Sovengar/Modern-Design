package com.jonathan.modern_design.user_module.role;

import com.jonathan.modern_design._infra.config.annotations.Fake;

import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class RoleRepoInMemory implements RoleRepo {
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
