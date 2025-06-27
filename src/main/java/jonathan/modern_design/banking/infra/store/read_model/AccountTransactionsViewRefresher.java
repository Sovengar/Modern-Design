package jonathan.modern_design.banking.infra.store.read_model;

import jakarta.persistence.EntityManager;
import jonathan.modern_design.banking.domain.events.TransactionRegistered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
class AccountTransactionsViewRefresher {
    private final EntityManager entityManager;

    @ApplicationModuleListener
    void refreshView(TransactionRegistered event) {
        log.info("Refreshing materialized view for accounts: [{}, {}]", event.sourceAccountNumber(), event.targetAccountNumber());
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW banking.transactions_by_account_view").executeUpdate();
    }

    @Scheduled(fixedRate = 30000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshView() {
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW CONCURRENTLY banking.transactions_by_account_view").executeUpdate();
    }
}
