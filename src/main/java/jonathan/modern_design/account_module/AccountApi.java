package jonathan.modern_design.account_module;

import jonathan.modern_design.account_module.application.AccountSearcher;
import jonathan.modern_design.account_module.domain.model.AccountNumber;
import jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import jonathan.modern_design.account_module.dtos.AccountResource;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;

import java.util.List;

public interface AccountApi {
    void transferMoney(final TransferMoneyCommand command);

    AccountResource findOne(final String accountNumber);

    List<AccountResource> search(final AccountSearcher.AccountSearchCriteria filters);

    void update(AccountResource dto);

    AccountNumber createAccount(final AccountCreatorCommand command);

    void deposit(final DepositCommand command);
}
