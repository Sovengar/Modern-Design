package jonathan.modern_design.banking.infra.store;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design.banking.domain.models.QTransaction;
import jonathan.modern_design.banking.domain.models.Transaction;
import jonathan.modern_design.banking.domain.store.TransactionRepo;
import jonathan.modern_design.banking.infra.store.spring.TransactionRepoSpringDataJPA;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataAdapter
@RequiredArgsConstructor
class TransactionStore implements TransactionRepo {
    private final TransactionRepoSpringDataJPA transactionRepoJPA;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void register(final Transaction transaction) {
        transactionRepoJPA.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        return queryFactory
                .selectFrom(QTransaction.transaction)
                .where(QTransaction.transaction.origin.eq(accountNumber).or(QTransaction.transaction.destination.eq(accountNumber)))
                .fetch();
    }

    @Override
    public Optional<LocalDateTime> findLastTransactionDate(String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        var transactions = queryFactory
                .selectFrom(QTransaction.transaction)
                .where(QTransaction.transaction.origin.eq(accountNumber).or(QTransaction.transaction.destination.eq(accountNumber)))
                .orderBy(QTransaction.transaction.transactionDate.desc())
                .fetch();

        if (transactions.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(transactions.getFirst().getTransactionDate());
        }
    }
}
