package jonathan.modern_design.account_module;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.application.AccountCRUDUpdater;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.AccountFinder;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.application.search.SearchAccount;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.infra.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountApi extends SearchAccount {
    //Commands
    void transferMoney(final MoneyTransfer.Command command);

    void update(AccountDto dto);

    AccountAccountNumber createAccount(final AccountCreator.Command command);

    void deposit(final Deposit.Command command);

    //Queries
    AccountDto findOne(final String accountNumber);
    //Queries from extends...

    @Injectable
    @RequiredArgsConstructor
    @Slf4j
    class AccountInternalApi implements AccountApi {
        private final AccountFinder accountFinder;
        private final SearchAccount searchAccount;
        private final MoneyTransfer moneyTransfer;
        private final AccountCreator accountCreator;
        private final AccountCRUDUpdater accountCRUDUpdater;
        private final Deposit deposit;

        @Override
        public void transferMoney(final MoneyTransfer.Command message) {
            moneyTransfer.handle(message);
        }

        @Override
        public void update(AccountDto dto) {
            accountCRUDUpdater.handle(dto);
        }

        @Override
        public AccountAccountNumber createAccount(final AccountCreator.Command message) {
            return accountCreator.handle(message);
        }

        @Override
        public void deposit(final Deposit.Command message) {
            deposit.handle(message);
        }

        //region Queries applying soft CQRS, we can skip service layer and access directly to repository
        //I think this should be moved to another facade
        @Override
        public AccountDto findOne(final String accountNumber) {
            return accountFinder.queryWith(accountNumber);
        }

        @Override
        public List<AccountSearchResult> searchWithJPQL(final Criteria filters) {
            return searchAccount.searchWithJPQL(filters);
        }

        @Override
        public List<AccountSearchResult> searchWithQueryDSL(final Criteria filters) {
            return searchAccount.searchWithQueryDSL(filters);
        }

        @Override
        public Optional<AccountDto> searchWithUserPassword(final String password) {
            return searchAccount.searchWithUserPassword(password);
        }

        @Override
        public List<AccountDto> searchForXXXPage(final Criteria filters) {
            return searchAccount.searchForXXXPage(filters);
        }

        @Override
        public Page<AccountDto> searchWithPagination(final Pageable pageable, final Criteria filters) {
            return searchAccount.searchWithPagination(pageable, filters);
        }
        //endregion
    }
}
