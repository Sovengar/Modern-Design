package jonathan.modern_design.banking.application.automatic_account_closer;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jonathan.modern_design._shared.domain.tags.ApplicationService;
import jonathan.modern_design._shared.domain.tags.WebAdapter;
import jonathan.modern_design.banking.application.DeactivateAccount;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;

import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@WebAdapter("/v1/internal/accounts/closer")
@Slf4j
@RequiredArgsConstructor
class AccountCloserInternalHttpController {
    private final AccountCloserCron accountCloserCron;

    @Operation(summary = "Close accounts that are inactive for a long time")
    @PostMapping(path = "/execute")
    public ResponseEntity<Void> closeInactiveAccounts() {
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN CloseInactiveAccounts cron job execution.");
        accountCloserCron.execute();
        log.info("END CloseInactiveAccounts cron job execution.");

        return ResponseEntity.ok().build();
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
class AccountCloserCron {
    private final AccountRepo accountRepo;
    private final DeactivateAccount deactivator;

    @Transactional
    @Scheduled(cron = "${accountDeactivator-cron}")
    void execute() {
        log.info("BEGIN CloseInactiveAccounts cron job execution.");
        var accounts = accountRepo.findAll();
        accounts.forEach(account -> deactivator.handle(account.getAccountNumber().getAccountNumber()));
        log.info("END CloseInactiveAccounts cron job execution.");
    }
}
