package jonathan.modern_design.account_module.api;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import jonathan.modern_design.account_module.application.CreateAccount;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.FindAccount;
import jonathan.modern_design.account_module.application.TransferMoney;
import jonathan.modern_design.account_module.application.UpdateAccountCRUD;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface AccountApi {
    void transferMoney(final TransferMoney.Command command);

    void update(AccountDto dto);

    AccountAccountNumber createAccount(final CreateAccount.Command command);

    void deposit(final Deposit.Command command);

    AccountDto findOne(final String accountNumber);

    @Injectable
    @RequiredArgsConstructor
    @Slf4j
    class AccountInternalApi implements AccountApi {
        private final FindAccount findAccount;
        private final TransferMoney transferMoney;
        private final CreateAccount createAccount;
        private final UpdateAccountCRUD updateAccountCRUD;
        private final Deposit deposit;

        @Override
        public void transferMoney(final TransferMoney.Command message) {
            transferMoney.handle(message);
        }

        @Override
        public void update(AccountDto dto) {
            updateAccountCRUD.handle(dto);
        }

        @Override
        public AccountAccountNumber createAccount(final CreateAccount.Command message) {
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
