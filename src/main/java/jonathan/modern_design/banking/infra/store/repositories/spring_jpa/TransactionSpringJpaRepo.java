package jonathan.modern_design.banking.infra.store.repositories.spring_jpa;

import jonathan.modern_design.banking.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionSpringJpaRepo extends JpaRepository<Transaction, Transaction.Id> {
}
