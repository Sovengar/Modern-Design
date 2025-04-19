package jonathan.modern_design.user.infra;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.repos.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

interface RoleRepoSpringDataJPA extends JpaRepository<Role, Role.Code> {
    Role findByDescription(String desc);
}

@Query
@RequiredArgsConstructor
class RoleRepoAdapter implements RoleRepo {
    private final RoleRepoSpringDataJPA repository;

    @Override
    public Role findByCode(Role.Code code) {
        return repository.findById(code).orElseThrow(EntityNotFoundException::new);
    }
}
