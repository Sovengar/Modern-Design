package jonathan.modern_design.account_module;

import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.infra.AccountSearchRepo;

public interface AccountApi extends AccountSearchRepo {
    //Commands
    void transferMoney(final MoneyTransfer.Command command);

    void update(AccountDto dto);

    AccountAccountNumber createAccount(final AccountCreator.Command command);

    void deposit(final Deposit.Command command);

    //Queries
    AccountDto findOne(final String accountNumber);
    //Queries from extends...
}
