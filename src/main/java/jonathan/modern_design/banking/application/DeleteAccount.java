package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.application.EntityDeleter;
import jonathan.modern_design._shared.domain.events.AccountHolderDeleted;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.api.events.AccountDeleted;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountHolderSpringJpaRepo;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountSpringJpaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
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

        log.info("Request arrived to DeleteAccount for accountNumber: {} and reason: {}", accountNumber, reason);
        var username = "Deleter"; //SecurityContextHolder.getContext().getAuthentication().getName();

        deleteAccount.handle(new DeleteAccount.Command(accountNumber, reason, username));

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
public class DeleteAccount {
    private final AccountSpringJpaRepo accountRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final EntityDeleter entityDeleter;
    private final AccountHolderSpringJpaRepo accountHolderRepo;

    @Transactional
    void handle(final @Valid Command message) {
        log.info("BEGIN DeleteAccount for accountNumber: {} and reason: {} by user: {}", message.accountNumber(), message.reason(), message.username());
        var account = accountRepo.findByAccNumberOrElseThrow(message.accountNumber());
        var accountHolder = account.getAccountHolder();

        //var username = ApplicationContext.getContext().getAuthentication().getName();

        entityDeleter.saveDeletedEntity(account, AccountEntity.DB_PATH, String.valueOf(account.getId()), message.username(), message.reason());
        entityDeleter.saveDeletedEntity(accountHolder, AccountHolder.DB_PATH, String.valueOf(accountHolder.getId()), message.username(), message.reason());

        accountRepo.delete(account);
        accountHolderRepo.delete(accountHolder);

        eventPublisher.publishEvent(new AccountDeleted(account.getAccountNumber()));
        eventPublisher.publishEvent(new AccountHolderDeleted(accountHolder.getId(), accountHolder.getUserId()));

        log.info("END DeleteAccount for accountNumber: {} and reason: {} by user: {}", message.accountNumber(), message.reason(), message.username());
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
