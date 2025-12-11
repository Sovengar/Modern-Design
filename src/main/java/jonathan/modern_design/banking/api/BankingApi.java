package jonathan.modern_design.banking.api;

import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.application.create_account.CreateAccountCommand;
import jonathan.modern_design.banking.domain.vo.AccountNumber;

//Here we have the behavior we want to expose to other modules, my UI can call more methods because is on the same logical boundary
public interface BankingApi {
    void transferMoney(final TransferMoney.Command command);

    AccountNumber createAccount(final CreateAccountCommand command);

    void deposit(final Deposit.Command command);
}
