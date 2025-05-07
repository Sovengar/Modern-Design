package jonathan.modern_design.account_module.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jonathan.modern_design._common.delete_table.DeletedRowService;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@WebAdapter("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
class DeleteAccountHttpController {
    private final DeleteAccount deleteAccount;

    @Observed(name = "deleteAccount")
    @Operation(summary = "Delete an account")
    @DeleteMapping(path = "/{accountNumber}/reason/{reason}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber, @PathVariable String reason) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        Assert.state(StringUtils.hasText(reason), "No reason provided");
        generateTraceId();

        log.info("BEGIN DeleteAccount for accountNumber: {} and reason: {}", accountNumber, reason);
        deleteAccount.handle(accountNumber, reason);
        log.info("END DeleteAccount for accountNumber: {} and reason: {}", accountNumber, reason);

        return ResponseEntity.ok().build();
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
class DeleteAccount {
    private final AccountRepo repository;
    private final DeletedRowService deletedRowService;

    @Transactional
    public void handle(final String accountNumber, final String reason) {
        log.info("BEGIN DeleteAccount");
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        deletedRowService.saveDeletedEntity(account, "md.accounts", String.valueOf(account.getAccountId().id()), username, reason);
        repository.delete(accountNumber);

        log.info("END DeleteAccount");
    }
}
