package jonathan.modern_design.auth.infra;

import jonathan.modern_design.banking.api.events.AccountSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class AccountReadModelConsumer {

    @ApplicationModuleListener
    void a(AccountSnapshot event) {
        log.info("Received AccountSnapshot event for accountNumber: {}, Data: {}", event.accountNumber(), event);
        //TODO UPDATE READ MODEL
        //TODO SEND THIS EVENT TO RABBITMQ TOPIC EXCHANGE OR KAFKA COMPACTED TOPIC
    }
}
