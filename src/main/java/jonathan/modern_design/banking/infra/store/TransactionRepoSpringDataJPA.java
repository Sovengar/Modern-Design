package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.banking.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionRepoSpringDataJPA extends JpaRepository<Transaction, Transaction.Id> {
}
