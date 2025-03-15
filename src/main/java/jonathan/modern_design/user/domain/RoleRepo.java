package jonathan.modern_design.user.domain;

public interface RoleRepo {
    Role findByCode(Role.Code code);
}
