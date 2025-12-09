package jonathan.modern_design.banking.infra.store.repositories.spring_jpa;

import jonathan.modern_design.banking.infra.store.readmodels.TransactionsByAccountView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountTransactionsViewSpringJpaRepo extends JpaRepository<TransactionsByAccountView, UUID> {
    List<TransactionsByAccountView> findByOriginAccountOrDestinationAccount(String accountNumber, String accountNumber2);
}
