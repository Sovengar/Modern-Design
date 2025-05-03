package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.Transaction;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class WithdrawMoneyHttpController {
    private final WithdrawMoney withdrawMoney;

    @PutMapping(path = "/{accountNumber}/withdraw/{amount}/{currency}")
    public ResponseEntity<Void> getBalance(@PathVariable String accountNumber, @PathVariable BigDecimal amount, @PathVariable String currency) {
        log.info("BEGIN Controller - WithdrawMoney");
        var withdrawMoneyCommand = new WithdrawMoney.WithdrawMoneyCommand(accountNumber, amount, Currency.fromCode(currency));
        withdrawMoney.handle(withdrawMoneyCommand);
        log.info("END Controller - WithdrawMoney");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
class WithdrawMoney {
    private final AccountRepo repository;
    private final TransactionRepo transactionRepo;

    public void handle(final @Valid WithdrawMoneyCommand message) {
        log.info("BEGIN WithdrawMoney");
        var account = repository.findByAccNumber(message.accountNumber()).orElseThrow();

        var money = AccountMoney.of(message.amount(), message.currency());
        account.withdrawal(money);
        var tx = Transaction.Factory.withdrawal(money, account.getAccountNumber().getAccountNumber());

        transactionRepo.register(tx);
        repository.update(account);
        log.info("END WithdrawMoney");
    }

    public record WithdrawMoneyCommand(
            @NotEmpty(message = "Account number is required") String accountNumber,
            @NotEmpty(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}
