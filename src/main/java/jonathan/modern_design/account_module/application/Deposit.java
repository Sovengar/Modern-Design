package jonathan.modern_design.account_module.application;

import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class DepositController {
    private final Deposit deposit;

    @PutMapping("/{accountNumber}/deposit/{amount}/{currency}")
    public void deposit(
            @PathVariable("accountNumber") String accountNumber,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency
    ) {
        log.info("BEGIN Controller - Deposit");
        final var command = new DepositCommand(accountNumber, amount, Currency.fromCode(currency));
        deposit.deposit(command);
        log.info("END Controller - Deposit");
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
public class Deposit {
    private final AccountRepo repository;

    public void deposit(final @Valid DepositCommand command) {
        log.info("BEGIN Deposit");
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        repository.update(account);
        log.info("END Deposit");
    }
}
