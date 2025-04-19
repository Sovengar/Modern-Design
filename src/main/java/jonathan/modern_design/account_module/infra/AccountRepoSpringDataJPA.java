package jonathan.modern_design.account_module.infra;

import jonathan.modern_design.account_module.domain.AccountEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepoSpringDataJPA extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}
