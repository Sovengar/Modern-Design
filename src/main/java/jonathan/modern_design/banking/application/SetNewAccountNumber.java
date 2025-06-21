package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
//Atomic Update, following Task UI Design
class SetNewAccountNumberHttpController {
    private final SetNewAccountNumber updater;

    @Operation(description = "SetNewAccountNumber")
    @PutMapping(value = "/setNewAccountNumber", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<String>> updateAccount(final String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN Updating account number of account: {}", accountNumber);
        var newAccountNumber = updater.handle(accountNumber);
        log.info("END Account with old number {} updated to number {}", accountNumber, newAccountNumber);

        return ResponseEntity.ok(new Response.Builder<String>().data(newAccountNumber).withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class SetNewAccountNumber {
    private final AccountRepo repository;

    String handle(final String accountNumber) {
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        account.generateNewAccountNumber();
        return account.getAccountNumber().getAccountNumber();
    }
}
