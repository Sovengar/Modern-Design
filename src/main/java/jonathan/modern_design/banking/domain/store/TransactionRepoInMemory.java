package jonathan.modern_design.banking.domain.store;

import jonathan.modern_design._shared.tags.Fake;
import jonathan.modern_design.banking.domain.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Fake
public class TransactionRepoInMemory implements TransactionRepo {
    private final ConcurrentHashMap<Transaction.Id, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void register(final Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(final String accountNumber) {
        return List.of();
    }

    @Override
    public Optional<LocalDateTime> findLastTransactionDate(final String accountNumber) {
        return Optional.empty();
    }
}
