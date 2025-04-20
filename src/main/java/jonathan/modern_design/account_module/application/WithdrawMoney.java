package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class WithdrawMoneyController {
    private final WithdrawMoney withdrawMoney;

    @PutMapping(path = "/{accountNumber}/withdraw/{amount}/{currency}")
    public ResponseEntity<Void> getBalance(@PathVariable String accountNumber, @PathVariable BigDecimal amount, @PathVariable String currency) {
        log.info("BEGIN Controller - WithdrawMoney");
        var withdrawMoneyCommand = new WithdrawMoney.WithdrawMoneyCommand(accountNumber, amount, Currency.fromCode(currency));
        withdrawMoney.withdraw(withdrawMoneyCommand);
        log.info("END Controller - WithdrawMoney");
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
class WithdrawMoney {
    private final AccountRepo repository;

    public void withdraw(final @Valid WithdrawMoneyCommand message) {
        log.info("BEGIN WithdrawMoney");
        var account = repository.findOne(message.accountNumber()).orElseThrow();
        account.subtract(message.amount(), message.currency());
        repository.update(account);
        log.info("END WithdrawMoney");
    }

    public record WithdrawMoneyCommand(String accountNumber, BigDecimal amount, Currency currency) {
    }
}
