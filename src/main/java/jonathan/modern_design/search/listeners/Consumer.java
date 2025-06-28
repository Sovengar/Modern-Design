package jonathan.modern_design.search.listeners;

import jonathan.modern_design._shared.events.banking.AccountSnapshot;
import jonathan.modern_design.search.read_models.AccountWithUserInfoReadModel;
import jonathan.modern_design.search.store.AccountWithUserInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class Consumer {
    private final AccountWithUserInfoRepo accountWithUserInfoRepo;

    //Migrate to ApplicationModuleListener when needed
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void a(AccountSnapshot event) {
        log.info("Received AccountSnapshot event for accountNumber: {}, Data: {}", event.accountNumber(), event);

        //var user = ???
        //TODO
        var readModel = new AccountWithUserInfoReadModel(UUID.randomUUID(), event.accountNumber(), event.money().getBalance(), "", "");
        accountWithUserInfoRepo.save(readModel);

        //TODO SEND THIS EVENT TO RABBITMQ TOPIC EXCHANGE OR KAFKA COMPACTED TOPIC
    }
}
