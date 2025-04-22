package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design.account_module.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionRepoSpringDataJPA extends JpaRepository<Transaction, Transaction.Id> {
}
