package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design.account_module.domain.AccountJdbcEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepoSpringDataJDBC extends CrudRepository<AccountJdbcEntity, String> {
    Optional<AccountJdbcEntity> findByAccountNumber(@NonNull String accountNumber);
}
