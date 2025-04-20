package jonathan.modern_design.user.infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.store.RoleStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Using JPA Repository as an easy way to query but is not an Aggregate Root
interface RoleRepoSpringDataJPA extends JpaRepository<Role, Role.Code> {
    default Role findByIdOrElseThrow(Role.Code code) {
        return findById(code).orElseThrow(EntityNotFoundException::new);
    }
}

@DataAdapter
@RequiredArgsConstructor
class RoleStoreAdapter implements RoleStore {
    @PersistenceContext
    private final EntityManager em;

    private final RoleRepoSpringDataJPA repository;

    @Override
    public Role findByCode(Role.Code code) {
        var role = em.createQuery("SELECT r FROM Role r WHERE r.code = :code", Role.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        return role != null ? role : repository.findByIdOrElseThrow(code);
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }
}
