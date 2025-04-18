package jonathan.modern_design.account_module;

import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.CreateAccountCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import jonathan.modern_design.account_module.infra.AccountSearchRepo;

public interface AccountApi extends AccountSearchRepo {
    //Commands
    void transferMoney(final TransferMoneyCommand command);

    void update(AccountDto dto);

    AccountAccountNumber createAccount(final CreateAccountCommand command);

    void deposit(final Deposit.DepositCommand command);

    //Queries
    AccountDto findOne(final String accountNumber);
    //Queries from extends...
}
