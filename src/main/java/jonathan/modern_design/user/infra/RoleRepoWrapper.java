package jonathan.modern_design.user.infra;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._common.annotations.Fake;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.RoleRepo;
import jonathan.modern_design.user.domain.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.concurrent.ConcurrentHashMap;

interface RoleSpringRepo extends JpaRepository<Role, Role.Code> {
    Role findByDescription(String desc);
}

//This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
@Fake
class RoleRepoInMemory implements RoleRepo {
    private final ConcurrentHashMap<Role.Code, Role> codes = new ConcurrentHashMap<>();

    public RoleRepoInMemory() {
        codes.put(new Role.Code(Roles.ADMIN.code()), Role.of(Roles.ADMIN));
        codes.put(new Role.Code(Roles.USER.code()), Role.of(Roles.USER));
        codes.put(new Role.Code(Roles.TECHNICIAN.code()), Role.of(Roles.TECHNICIAN));
    }

    @Override
    public Role findByCode(final Role.Code code) {
        return codes.get(code);
    }
}

@Query
@RequiredArgsConstructor
class RoleRepoAdapter implements RoleRepo {
    private final RoleSpringRepo repository;

    @Override
    public Role findByCode(Role.Code code) {
        return repository.findById(code).orElseThrow(EntityNotFoundException::new);
    }
}
