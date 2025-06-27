package jonathan.modern_design.banking.infra.store.read_model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountTransactionsViewRepository extends JpaRepository<TransactionsByAccountView, UUID> {
    List<TransactionsByAccountView> findByOriginAccountOrDestinationAccount(String accountNumber, String accountNumber2);
}
