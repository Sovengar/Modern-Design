package jonathan.modern_design.account_module.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class TransferMoneyController {
    private final TransferMoney transferMoney;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{balance}/{currency}")
    void transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("balance") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);

        val command = new TransferMoney.Command(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        transferMoney.handle(command);

        log.info("END Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
public class TransferMoney {
    private final AccountRepo repository;
    private final AccountValidator accountValidator;

    @Transactional
    public void handle(final @Valid Command message) {
        log.info("BEGIN TransferMoney");

        Account source = getAccountValidated(message.sourceId());
        Account target = getAccountValidated(message.targetId());

        validateDifferentAccounts(source.accountAccountNumber(), target.accountAccountNumber());

        final var amount = message.amount();
        final var currency = message.currency();

        handle(source, target, amount, currency);
        log.info("END TransferMoney");
    }

    private Account getAccountValidated(final String accountNumber) {
        var account = repository.findOneOrElseThrow(accountNumber);
        accountValidator.validateAccount(account);
        return account;
    }

    private void validateDifferentAccounts(final AccountAccountNumber source, final AccountAccountNumber target) {
        var isSameAccount = source.equals(target);

        if (isSameAccount) {
            throw new OperationForbiddenForSameAccount();
        }
    }

    private void handle(Account source, Account target, final BigDecimal amount, final Currency currency) {
        source.subtract(amount, currency);
        target.add(amount, currency);

        repository.update(source);
        repository.update(target);
    }

    public record Command(
            @NotEmpty(message = "Source Account is required") String sourceId,
            @NotEmpty(message = "Target Account is required") String targetId,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotEmpty(message = "Currencyis required") Currency currency) {
    }
}
