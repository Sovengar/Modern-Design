package jonathan.modern_design.auth.infra.store.repositories.postgres;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.store.RoleStore;
import jonathan.modern_design.auth.infra.store.repositories.spring_jpa.RoleSpringJpaRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DataAdapter
@RequiredArgsConstructor
class RolePostgresRepo implements RoleStore {
    @PersistenceContext
    private final EntityManager em;

    private final RoleSpringJpaRepo repository;

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
