package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design.account_module.domain.models.Transaction;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionRepoSpringDataJPA extends JpaRepository<Transaction, Transaction.Id>, TransactionRepo {

    @Override
    default void register(final Transaction transaction) {
        save(transaction);
    }
}
