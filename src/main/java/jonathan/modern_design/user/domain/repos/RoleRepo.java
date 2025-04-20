package jonathan.modern_design.user.domain.repos;

import jonathan.modern_design.user.domain.Role;

import java.util.List;

public interface RoleRepo {
    Role findByCode(Role.Code code);

    List<Role> findAll();
}
