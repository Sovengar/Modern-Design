package jonathan.modern_design.account_module;

import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.application.search.SearchAccount;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.infra.AccountDto;

public interface AccountApi extends SearchAccount {
    //Commands
    void transferMoney(final MoneyTransfer.Command command);

    void update(AccountDto dto);

    AccountAccountNumber createAccount(final AccountCreator.Command command);

    void deposit(final Deposit.Command command);

    //Queries
    AccountDto findOne(final String accountNumber);
    //Queries from extends...
}
