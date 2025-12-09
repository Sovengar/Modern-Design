package jonathan.modern_design.auth.infra.store.repositories.spring_jpa;

import jakarta.persistence.EntityNotFoundException;
import jonathan.modern_design.auth.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

//Using JPA Repository as an easy way to query but is not an Aggregate Root
public interface RoleSpringJpaRepo extends JpaRepository<Role, Role.Code> {
    default Role findByIdOrElseThrow(Role.Code code) {
        return findById(code).orElseThrow(EntityNotFoundException::new);
    }
}
