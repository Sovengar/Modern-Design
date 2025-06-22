package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.domain.tags.ApplicationService;
import jonathan.modern_design._shared.domain.tags.WebAdapter;
import jonathan.modern_design.banking.domain.models.Account;
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
class ActivateAccountHttpController {
    private final ActivateAccount activateAccount;

    @Operation(summary = "Activate an account")
    @PutMapping(path = "/{accountNumber}/activate")
    public ResponseEntity<Response<Void>> activate(final @PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN ActivateAccount for accountNumber: {}", accountNumber);
        activateAccount.handle(new ActivateAccount.Command(accountNumber));
        log.info("END ActivateAccount for accountNumber: {}", accountNumber);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@RequiredArgsConstructor
@ApplicationService
class ActivateAccount {
    private final AccountRepo repository;

    protected void handle(final @Valid Command message) {
        var account = repository.findByAccNumberOrElseThrow(message.accountNumber());
        var instrumentation = new ActivateAccountInstrumentation(account);

        instrumentation.voidExecutionWithLogs(() -> {
            account.activate();
            repository.update(account);
        }, "Activate account");

        instrumentation.accountActivated();
    }

    record Command(@NotEmpty(message = "Account number is required") String accountNumber) {
    }
}

//Overengineering, maybe useful when you need to compose various variables on every log like accountId, accountNumber, userId and some dynamic others
@Slf4j
@RequiredArgsConstructor
class ActivateAccountInstrumentation {
    private static final String START = "Executing action {}";
    private static final String END = "Action {} executed successfully";

    private final Account account;

    public void accountActivated() {
        log.info("Account with id: {} and number: {} has been activated", account.getAccountId().id(), account.getAccountNumber().getAccountNumber());
    }

    public void voidExecutionWithLogs(Runnable action, String actionName) {
        log.info(START, actionName);
        action.run();
        log.info(END, actionName);
    }
}
