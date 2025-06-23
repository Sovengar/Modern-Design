package jonathan.modern_design.banking.infra.store.spring;

import jonathan.modern_design.banking.domain.models.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AccountHolderRepoSpringDataJPA extends JpaRepository<AccountHolder, UUID> {

}
