package jonathan.modern_design.banking.queries;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.infra.store.read_model.AccountTransactionsViewRepository;
import jonathan.modern_design.banking.infra.store.read_model.TransactionsByAccountView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@WebAdapter("/v1/accounts")
@Slf4j
@RequiredArgsConstructor
class FindAccountWithTransactionsHttpController {
    private final FindAccountWithTransactions querier;

    @GetMapping("/{accountNumber}/transactions")
    public List<TransactionsByAccountView> getTransactions(@PathVariable String accountNumber) {
        log.info("BEGIN getTransactions for accountNumber: {}", accountNumber);
        var transactions = querier.getTransactions(accountNumber);
        log.info("END getTransactions for accountNumber: {}", accountNumber);
        return transactions;
    }
}

@DataAdapter
class FindAccountWithTransactions {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final AccountTransactionsViewRepository viewRepository;

    public FindAccountWithTransactions(EntityManager entityManager, AccountTransactionsViewRepository viewRepository) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
        this.viewRepository = viewRepository;
    }

    public List<TransactionsByAccountView> getTransactions(String accountNumber) {
        return viewRepository.findByOriginAccountOrDestinationAccount(accountNumber, accountNumber);
    }
}
