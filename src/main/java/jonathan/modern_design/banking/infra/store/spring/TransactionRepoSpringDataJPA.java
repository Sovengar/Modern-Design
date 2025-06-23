package jonathan.modern_design.banking.infra.store.spring;

import jonathan.modern_design.banking.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepoSpringDataJPA extends JpaRepository<Transaction, Transaction.Id> {
}
