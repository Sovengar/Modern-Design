package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.domain.exceptions.OperationForbiddenForSameAccount;
import jonathan.modern_design.banking.domain.models.Transaction;
import jonathan.modern_design.banking.domain.models.account.Account;
import jonathan.modern_design.banking.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.banking.domain.models.account.vo.AccountNumber;
import jonathan.modern_design.banking.domain.services.AccountValidator;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

import static jonathan.modern_design._shared.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class TransferMoneyHttpController {
    private final TransferMoney transferMoney;

    @Operation(summary = "Transfer money from one account to another")
    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{balance}/{currency}")
    ResponseEntity<Response<Void>> transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("balance") BigDecimal amount,
            @PathVariable("currency") String currency) {
        generateTraceId();
        //Authentication + Authorization

        val command = new TransferMoney.Command(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        log.info("BEGIN Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);
        transferMoney.handle(command);
        log.info("END Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class TransferMoney {
    private final AccountRepo repository;
    private final TransactionRepo transactionRepo;
    private final AccountValidator accountValidator;

    @Transactional
    public void handle(final @Valid Command message) {
        log.info("BEGIN TransferMoney");

        Account source = getAccountValidated(message.sourceId());
        Account target = getAccountValidated(message.targetId());

        validateDifferentAccounts(source.getAccountNumber(), target.getAccountNumber());

        final var amount = message.amount();
        final var currency = message.currency();

        transfer(source, target, amount, currency);
        log.info("END TransferMoney");
    }

    private Account getAccountValidated(final String accountNumber) {
        var account = repository.findByAccNumberOrElseThrow(accountNumber);
        accountValidator.validateAccount(account);
        return account;
    }

    private void validateDifferentAccounts(final AccountNumber source, final AccountNumber target) {
        var isSameAccount = source.equals(target);

        if (isSameAccount) {
            throw new OperationForbiddenForSameAccount();
        }
    }

    private void transfer(Account source, Account target, final BigDecimal amount, final Currency currency) {
        var money = AccountMoney.of(amount, currency);

        source.withdrawal(money);
        target.deposit(money);

        repository.update(source);
        repository.update(target);

        var tx = Transaction.Factory.transfer(money, source.getAccountNumber().getAccountNumber(), target.getAccountNumber().getAccountNumber());
        transactionRepo.register(tx);
    }

    public record Command(
            @NotEmpty(message = "Source Account is required") String sourceId,
            @NotEmpty(message = "Target Account is required") String targetId,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}
