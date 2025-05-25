package jonathan.modern_design.account_module.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._common.api.Response;
import jonathan.modern_design._common.delete_table.DeletedRowService;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@WebAdapter("/v1/accounts")
@Slf4j
@RequiredArgsConstructor
class DeleteAccountHttpController {
    private final DeleteAccount deleteAccount;

    @Operation(summary = "Delete an account")
    @DeleteMapping(path = "/{accountNumber}/reason/{reason}")
    public ResponseEntity<Response<Void>> deleteAccount(@PathVariable String accountNumber, @PathVariable String reason) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        Assert.state(StringUtils.hasText(reason), "No reason provided");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN DeleteAccount for accountNumber: {} and reason: {}", accountNumber, reason);
        var username = "Deleter"; //SecurityContextHolder.getContext().getAuthentication().getName();

        deleteAccount.handle(new DeleteAccount.Command(accountNumber, reason, username));
        log.info("END DeleteAccount for accountNumber: {} and reason: {}", accountNumber, reason);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
public class DeleteAccount {
    private final AccountRepo repository;
    private final DeletedRowService deletedRowService;

    @Transactional
    void handle(final @Valid Command message) {
        log.info("BEGIN DeleteAccount");
        var account = repository.findByAccNumberOrElseThrow(message.accountNumber());

        deletedRowService.saveDeletedEntity(account, "md.accounts", String.valueOf(account.getAccountId().id()), message.username(), message.reason());
        repository.delete(message.accountNumber());

        //TODO DELETE USER DIRECTLY OR PUBLISHING AN EVENT

        log.info("END DeleteAccount");
    }

    record Command(
            @NotEmpty(message = "Account number is required")
            String accountNumber,
            @NotEmpty(message = "Reason is required")
            String reason,
            @NotEmpty(message = "Username is required")
            String username
    ) {
    }
}
