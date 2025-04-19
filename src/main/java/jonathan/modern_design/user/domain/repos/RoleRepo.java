package jonathan.modern_design.user.domain.repos;

import jonathan.modern_design.user.domain.Role;

public interface RoleRepo {
    Role findByCode(Role.Code code);
}
