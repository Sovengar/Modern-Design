package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class DepositController {
    private final Deposit deposit;

    @PutMapping("/{accountNumber}/deposit/{amount}/{currency}")
    public ResponseEntity<Void> deposit(
            @PathVariable("accountNumber") String accountNumber,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency
    ) {
        log.info("BEGIN Controller - Deposit");
        final var command = new Deposit.Command(accountNumber, amount, Currency.fromCode(currency));
        deposit.handle(command);
        log.info("END Controller - Deposit");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class Deposit {
    private final AccountRepo repository;

    @Transactional
    public void handle(final @Valid Command message) {
        log.info("BEGIN Deposit");
        var account = repository.findOne(message.accountNumber()).orElseThrow();
        var money = AccountMoney.of(message.amount(), message.currency());
        account.deposit(money);
        repository.update(account);
        log.info("END Deposit");
    }

    public record Command(
            @NotEmpty(message = "Account number is required") String accountNumber,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}


