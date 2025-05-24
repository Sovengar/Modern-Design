package jonathan.modern_design.account_module.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class DeactivateAccountHttpController {
    private final DeactivateAccount deactivateAccount;

    @Observed(name = "deactivateAccount")
    @Operation(description = "Deactivate an account")
    @PutMapping(path = "/{accountNumber}/deactivate")
    public ResponseEntity<Void> deactivate(final @PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();

        log.info("BEGIN DeactivateAccount for accountNumber: {}", accountNumber);
        deactivateAccount.handle(accountNumber);
        log.info("END DeactivateAccount for accountNumber: {}", accountNumber);

        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class DeactivateAccount {
    private final AccountRepo repository;

    public void handle(final String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");

        log.info("BEGIN - DeactiveAccount");
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        account.deactivate();
        repository.update(account);
        log.info("END - DeactiveAccount");
    }


}
