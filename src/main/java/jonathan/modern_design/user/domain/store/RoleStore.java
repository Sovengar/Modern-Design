package jonathan.modern_design.user.domain.store;

import jonathan.modern_design.user.domain.Role;

import java.util.List;

public interface RoleStore {
    Role findByCode(Role.Code code);

    List<Role> findAll();
}
