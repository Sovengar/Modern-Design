package jonathan.modern_design.auth.infra.store.repositories.spring_jpa;

import jonathan.modern_design.auth.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSpringJpaRepo extends JpaRepository<User, User.Id> {
}
