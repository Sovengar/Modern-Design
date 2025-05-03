package jonathan.modern_design.account_module.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design._common.annotations.ApplicationService;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import jonathan.modern_design.account_module.domain.models.Transaction;
import jonathan.modern_design.account_module.domain.models.account.Account;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import jonathan.modern_design.account_module.domain.store.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class TransferMoneyHttpController {
    private final TransferMoney transferMoney;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{balance}/{currency}")
    ResponseEntity<Void> transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("balance") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);

        val command = new TransferMoney.Command(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        transferMoney.handle(command);

        log.info("END Controller - Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);
        return ResponseEntity.ok().build();
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

        validateDifferentAccounts(source.accountAccountNumber(), target.accountAccountNumber());

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

    private void validateDifferentAccounts(final AccountAccountNumber source, final AccountAccountNumber target) {
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

        var tx = Transaction.Factory.transfer(money, source.accountAccountNumber().accountNumber(), target.accountAccountNumber().accountNumber());
        transactionRepo.register(tx);
    }

    public record Command(
            @NotEmpty(message = "Source Account is required") String sourceId,
            @NotEmpty(message = "Target Account is required") String targetId,
            @NotNull(message = "Amount is required") BigDecimal amount,
            @NotNull(message = "Currency is required") Currency currency) {
    }
}
