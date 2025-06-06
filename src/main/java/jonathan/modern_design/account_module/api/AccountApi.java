package jonathan.modern_design.account_module.api;

import jonathan.modern_design._common.tags.Injectable;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import jonathan.modern_design.account_module.application.CreateAccount;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.GenericUpdateAccount;
import jonathan.modern_design.account_module.application.TransferMoney;
import jonathan.modern_design.account_module.application.queries.FindAccount;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Here we have the behavior we want to expose to other modules, my UI can call more methods because is on the same logical boundary
public interface AccountApi {
    void transferMoney(final TransferMoney.Command command);

    AccountNumber createAccount(final CreateAccount.Command command);

    void deposit(final Deposit.Command command);

    AccountDto findOne(final String accountNumber);

    @Injectable
    @RequiredArgsConstructor
    @Slf4j
    class Internal implements AccountApi {
        private final FindAccount findAccount;
        private final TransferMoney transferMoney;
        private final CreateAccount createAccount;
        private final GenericUpdateAccount genericUpdateAccount;
        private final Deposit deposit;

        @Override
        public void transferMoney(final TransferMoney.Command message) {
            transferMoney.handle(message);
        }

        public void update(AccountDto dto) {
            genericUpdateAccount.handle(dto);
        }

        @Override
        public AccountNumber createAccount(final CreateAccount.Command message) {
            return createAccount.handle(message);
        }

        @Override
        public void deposit(final Deposit.Command message) {
            deposit.handle(message);
        }

        @Override
        public AccountDto findOne(final String accountNumber) {
            return findAccount.queryWith(accountNumber);
        }
    }
}
