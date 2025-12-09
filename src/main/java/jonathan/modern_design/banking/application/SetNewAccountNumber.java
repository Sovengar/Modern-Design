package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;

import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
//Atomic Update, following Task UI Design
class SetNewAccountNumberHttpController {
    private final SetNewAccountNumber updater;

    @Operation(description = "SetNewAccountNumber")
    @PutMapping(value = "/setNewAccountNumber", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<String>> updateAccount(final String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("Request arrived to Updating account number of account: {}", accountNumber);
        var newAccountNumber = updater.handle(accountNumber);

        return ResponseEntity.ok(new Response.Builder<String>().data(newAccountNumber).withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class SetNewAccountNumber {
    private final AccountRepo repository;
    private final AccountNumberGenerator accountNumberGenerator;

    String handle(final String accountNumber) {
        log.info("BEGIN - Updating account number of account: {}", accountNumber);
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        account.generateNewAccountNumber(accountNumberGenerator);
        repository.update(account);
        log.info("END - Updating account number of account: {}", accountNumber);

        return account.getAccountNumber().getAccountNumber();
    }
}
