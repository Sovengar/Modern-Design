package jonathan.modern_design.auth.domain.store;

import jonathan.modern_design._shared.domain.tags.Fake;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
public class RoleStoreInMemory implements RoleStore {
    private final ConcurrentHashMap<Role.Code, Role> codes = new ConcurrentHashMap<>();
    private final List<Role> list = new ArrayList<>();

    public RoleStoreInMemory() {
        list.add(Role.of(Roles.ADMIN));
        list.add(Role.of(Roles.USER));
        list.add(Role.of(Roles.TECHNICIAN));

        list.forEach(role -> codes.put(role.getCode(), role));

        //codes.put(new Role.Code(Roles.ADMIN.code()), Role.of(Roles.ADMIN));
        //codes.put(new Role.Code(Roles.USER.code()), Role.of(Roles.USER));
        //codes.put(new Role.Code(Roles.TECHNICIAN.code()), Role.of(Roles.TECHNICIAN));
    }

    @Override
    public Role findByCode(final Role.Code code) {
        return codes.get(code);
    }

    @Override
    public List<Role> findAll() {
        return list;
    }
}
