package com.jonathan.modern_design.user_module.role;

import com.jonathan.modern_design._infra.config.annotations.Fake;

import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class RoleRepoInMemory implements RoleRepo {
    private final ConcurrentHashMap<Role.Code, Role> codes = new ConcurrentHashMap<>();

    @Override
    public Role findByCode(final Role.Code code) {
        return codes.get(code);
    }

    @Override
    public Role findByCode(final String desc) {
        return null;
    }
}
