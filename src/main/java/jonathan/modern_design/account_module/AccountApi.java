package jonathan.modern_design.account_module;

import jonathan.modern_design.account_module.domain.vo.AccountNumber;
import jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import jonathan.modern_design.account_module.infra.AccountSearchRepo;

public interface AccountApi extends AccountSearchRepo {
    //Commands
    void transferMoney(final TransferMoneyCommand command);

    void update(AccountDto dto);

    AccountNumber createAccount(final AccountCreatorCommand command);

    void deposit(final DepositCommand command);

    //Queries
    AccountDto findOne(final String accountNumber);
    //Queries from extends...
}
