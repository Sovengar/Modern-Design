package jonathan.modern_design.user.domain.repos;

import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.catalogs.Roles;

import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class RoleInMemoryRepo implements RoleRepo {
    private final ConcurrentHashMap<Role.Code, Role> codes = new ConcurrentHashMap<>();

    public RoleInMemoryRepo() {
        codes.put(new Role.Code(Roles.ADMIN.code()), Role.of(Roles.ADMIN));
        codes.put(new Role.Code(Roles.USER.code()), Role.of(Roles.USER));
        codes.put(new Role.Code(Roles.TECHNICIAN.code()), Role.of(Roles.TECHNICIAN));
    }

    @Override
    public Role findByCode(final Role.Code code) {
        return codes.get(code);
    }
}
