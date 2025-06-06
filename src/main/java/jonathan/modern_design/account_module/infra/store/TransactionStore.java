package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design._common.tags.DataAdapter;
import jonathan.modern_design.account_module.domain.models.Transaction;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataAdapter
@RequiredArgsConstructor
class TransactionStore implements TransactionRepo {
    private final TransactionRepoSpringDataJPA transactionRepoJPA;

    @Override
    public void register(final Transaction transaction) {
        transactionRepoJPA.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        //TODO CREATE QUERY ON SPRING DATA OR BUILT WITH ENTITYMANAGER OR QUERYDSL
        return List.of();
    }

    @Override
    public Optional<LocalDateTime> findLastTransactionDate(String accountNumber) {
        //TODO CREATE QUERY ON SPRING DATA OR BUILT WITH ENTITYMANAGER OR QUERYDSL
        return Optional.empty();
    }
}
