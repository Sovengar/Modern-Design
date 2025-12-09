package jonathan.modern_design.banking.infra.store.repositories.spring_jpa;

import jonathan.modern_design.banking.domain.models.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AccountHolderSpringJpaRepo extends JpaRepository<AccountHolder, UUID> {

}
