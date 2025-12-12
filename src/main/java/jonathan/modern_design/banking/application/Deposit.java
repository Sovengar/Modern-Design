package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.domain.catalogs.Currency;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.domain.models.Transaction;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import jonathan.modern_design.banking.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.Serial;
import java.math.BigDecimal;

import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
class DepositHttpController {
    private final Deposit deposit;

    @Operation(summary = "Deposit money to an account")
    @PutMapping("/{accountNumber}/deposit/{amount}/{currency}")
    public ResponseEntity<Void> deposit(
            @PathVariable("accountNumber") String accountNumber,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency
    ) {
        generateTraceId();
        //Authentication + Authorization

        final var message = new Deposit.Command(accountNumber, amount, Currency.fromCode(currency));

        log.info("Request arrived to Deposit for accountNumber: {} with amount: {} and currency: {}", accountNumber, amount, currency);
        deposit.handle(message);

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
        log.info("BEGIN Deposit for accountNumber: {} with amount: {} and currency: {}", message.accountNumber(), message.amount(), message.currency());
        var account = repository.findByAccNumberOrElseThrow(message.accountNumber());

        var money = Money.of(message.amount(), message.currency());
        account.deposit(money);
        var tx = Transaction.Factory.withdrawal(money, account.getAccountNumber());

        transactionRepo.register(tx);
        repository.update(account);
        log.info("END Deposit for accountNumber: {} with amount: {} and currency: {}", message.accountNumber(), message.amount(), message.currency());
    }

    public record Command(
            @NotEmpty(message = "Account number is required") String accountNumber,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }

    public static final class DepositLimitExceeded extends RuntimeException {
        @Serial private static final long serialVersionUID = -1450858818814932050L;
        private final String accountId;
        private final long attemptedAmount;
        private final long dailyLimit;

        public DepositLimitExceeded(String accountId, long attemptedAmount, long dailyLimit) {
            super("Deposit amount exceeds the daily limit");
            this.accountId = accountId;
            this.attemptedAmount = attemptedAmount;
            this.dailyLimit = dailyLimit;
        }

        public String accountId() {
            return accountId;
        }

        public long attemptedAmount() {
            return attemptedAmount;
        }

        public long dailyLimit() {
            return dailyLimit;
        }
    }
}




