package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.exceptions.OperationForbiddenForSameAccount;
import jonathan.modern_design.account_module.domain.services.AccountValidator;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Injectable
@RequiredArgsConstructor
public class MoneyTransfer {
    private final AccountRepo repository;
    private final AccountValidator accountValidator;

    public void transferMoney(final TransferMoneyCommand command) {
        Account source = getAccountValidated(command.sourceId());
        Account target = getAccountValidated(command.targetId());

        validateDifferentAccounts(source.accountAccountNumber(), target.accountAccountNumber());

        final var amount = command.amount();
        final var currency = command.currency();

        transferMoney(source, target, amount, currency);
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

}
