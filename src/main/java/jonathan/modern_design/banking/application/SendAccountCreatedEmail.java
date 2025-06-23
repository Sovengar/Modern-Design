package jonathan.modern_design.banking.application;

import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design.banking.api.events.AccountCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;

@ApplicationService
@Slf4j
class SendAccountCreatedEmail {

    @ApplicationModuleListener
    public void handle(AccountCreated event) {
        log.info("Sending fake email to {}", event.accountNumber());
    }
}
