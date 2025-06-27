package jonathan.modern_design.search.store;

import jonathan.modern_design.search.read_models.AccountWithUserInfoReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountWithUserInfoRepo extends JpaRepository<AccountWithUserInfoReadModel, UUID> {
}
