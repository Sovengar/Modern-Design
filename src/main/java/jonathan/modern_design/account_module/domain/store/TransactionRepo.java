package jonathan.modern_design.account_module.domain.store;

import jonathan.modern_design.account_module.domain.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepo {
    void register(final Transaction transaction);

    List<Transaction> findByAccountNumber(String accountNumber);

    Optional<LocalDateTime> findLastTransactionDate(String accountNumber);
}
