package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class DeactivateAccountHttpController {
    private final DeactivateAccount deactivateAccount;

    @Operation(description = "Deactivate an account")
    @PutMapping(path = "/{accountNumber}/deactivate")
    public ResponseEntity<Response<Void>> deactivate(final @PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN DeactivateAccount for accountNumber: {}", accountNumber);
        deactivateAccount.handle(accountNumber);
        log.info("END DeactivateAccount for accountNumber: {}", accountNumber);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
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
