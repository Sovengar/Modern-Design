package jonathan.modern_design.account_module.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
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
class MoneyTransferController {
    private final MoneyTransfer moneyTransfer;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{balance}/{currency}")
    void transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("balance") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);

        val command = new MoneyTransfer.Command(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        moneyTransfer.transferMoney(command);

        log.info("END Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);
    }
}

@Slf4j
@Injectable
@RequiredArgsConstructor
@Validated
public class MoneyTransfer {
    private final AccountRepo repository;
    private final AccountValidator accountValidator;

    @Transactional
    public void transferMoney(final @Valid Command message) {
        log.info("BEGIN TransferMoney");

        Account source = getAccountValidated(message.sourceId());
        Account target = getAccountValidated(message.targetId());

        validateDifferentAccounts(source.accountAccountNumber(), target.accountAccountNumber());

        final var amount = message.amount();
        final var currency = message.currency();

        transferMoney(source, target, amount, currency);
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

    private void transferMoney(Account source, Account target, final BigDecimal amount, final Currency currency) {
        source.subtract(amount, currency);
        target.add(amount, currency);

        repository.update(source);
        repository.update(target);
    }

    public record Command(String sourceId, String targetId, BigDecimal amount, Currency currency) {
    }
}
