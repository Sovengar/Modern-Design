package jonathan.modern_design.banking.application;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._shared.api.Response;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
class WithdrawMoneyHttpController {
    private final WithdrawMoney withdrawMoney;

    @Operation(summary = "Withdraw money from an account")
    @PutMapping(path = "/{accountNumber}/withdraw/{amount}/{currency}")
    public ResponseEntity<Response<Void>> getBalance(@PathVariable String accountNumber, @PathVariable BigDecimal amount, @PathVariable String currency) {
        generateTraceId();
        //Authentication + Authorization

        var withdrawMoneyCommand = new WithdrawMoney.WithdrawMoneyCommand(accountNumber, amount, Currency.fromCode(currency));

        log.info("Request arrived to WithdrawMoney for accountNumber: {} with amount: {} and currency: {}", accountNumber, amount, currency);
        withdrawMoney.handle(withdrawMoneyCommand);

        return ResponseEntity.ok(new Response.Builder<Void>().withDefaultMetadataV1());
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class WithdrawMoney {
    private final AccountRepo repository;
    private final TransactionRepo transactionRepo;

    public void handle(final @Valid WithdrawMoneyCommand message) {
        log.info("BEGIN WithdrawMoney for accountNumber: {} with amount: {} and currency: {}", message.accountNumber(), message.amount(), message.currency());
        var account = repository.findByAccNumber(message.accountNumber()).orElseThrow();

        var money = Money.of(message.amount(), message.currency());
        account.withdrawal(money);
        var tx = Transaction.Factory.withdrawal(money, account.getAccountNumber().getAccountNumber());

        transactionRepo.register(tx);
        repository.update(account);
        log.info("END WithdrawMoney for accountNumber: {} with amount: {} and currency: {}", message.accountNumber(), message.amount(), message.currency());
    }

    public record WithdrawMoneyCommand(
            @NotEmpty(message = "Account number is required") String accountNumber,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}
