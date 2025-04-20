package jonathan.modern_design.user.infra;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.repos.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface RoleRepoSpringDataJPA extends JpaRepository<Role, Role.Code> {
    default Role findByIdOrElseThrow(Role.Code code) {
        return findById(code).orElseThrow(EntityNotFoundException::new);
    }
}

@Query
@RequiredArgsConstructor
class RoleRepoAdapter implements RoleRepo {
    private final RoleRepoSpringDataJPA repository;

    @Override
    public Role findByCode(Role.Code code) {
        return repository.findByIdOrElseThrow(code);
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }
}
