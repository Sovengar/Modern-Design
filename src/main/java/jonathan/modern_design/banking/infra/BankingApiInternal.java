package jonathan.modern_design.banking.infra;

import jonathan.modern_design._shared.tags.Facade;
import jonathan.modern_design.banking.api.BankingApi;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.application.DeactivateAccount;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.application.GenericUpdateAccount;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.application.create_account.CreateAccount;
import jonathan.modern_design.banking.application.create_account.CreateAccountCommand;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Facade
@RequiredArgsConstructor
@Slf4j
public class BankingApiInternal implements BankingApi {
    private final TransferMoney transferMoney;
    private final CreateAccount createAccount;
    private final GenericUpdateAccount genericUpdateAccount;
    private final Deposit deposit;

    private final DeactivateAccount deactivateAccount;

    @Override
    public void transferMoney(final TransferMoney.Command message) {
        transferMoney.handle(message);
    }

    public void update(AccountDto dto) {
        genericUpdateAccount.handle(dto);
    }

    @Override
    public AccountNumber createAccount(final CreateAccountCommand message) {
        return createAccount.handle(message);
    }

    @Override
    public void deposit(final Deposit.Command message) {
        deposit.handle(message);
    }

    //TODO DOES THIS MAKE SENSE?
    private void deactivateAccount(String accountNumber) {
        deactivateAccount.handle(accountNumber);
    }
}
