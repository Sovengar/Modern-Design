package jonathan.modern_design.account_module.infra;

import jonathan.modern_design.account_module.domain.AccountEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepoSpringDataJDBC extends CrudRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}
