package jonathan.modern_design.auth.domain.store;

import jonathan.modern_design.auth.domain.models.Role;

import java.util.List;

public interface RoleStore {
    Role findByCode(Role.Code code);

    List<Role> findAll();
}
