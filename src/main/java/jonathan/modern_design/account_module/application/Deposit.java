package jonathan.modern_design.account_module.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.Transaction;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class DepositHttpController {
    private final Deposit deposit;

    @Observed(name = "deposit")
    @Operation(summary = "Deposit money to an account")
    @PutMapping("/{accountNumber}/deposit/{amount}/{currency}")
    public ResponseEntity<Void> deposit(
            @PathVariable("accountNumber") String accountNumber,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency
    ) {
        generateTraceId();
        final var message = new Deposit.Command(accountNumber, amount, Currency.fromCode(currency));

        log.info("BEGIN Deposit for accountNumber: {} with amount: {} and currency: {}", accountNumber, amount, currency);
        deposit.handle(message);
        log.info("END Deposit for accountNumber: {} with amount: {} and currency: {}", accountNumber, amount, currency);

        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class Deposit {
    private final AccountRepo repository;
    private final TransactionRepo transactionRepo;

    @Transactional
    public void handle(final @Valid Command message) {
        log.info("BEGIN Deposit");
        var account = repository.findByAccNumberOrElseThrow(message.accountNumber());

        var money = AccountMoney.of(message.amount(), message.currency());
        account.deposit(money);
        var tx = Transaction.Factory.withdrawal(money, account.getAccountNumber().getAccountNumber());

        transactionRepo.register(tx);
        repository.update(account);
        log.info("END Deposit");
    }

    public record Command(
            @NotEmpty(message = "Account number is required") String accountNumber,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}


