package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.application.AccountCRUDUpdater;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.application.SearchAccount;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Injectable
@RequiredArgsConstructor
@Slf4j
class AccountFacade implements AccountApi {
    private final AccountRepo repository;
    private final SearchAccount searchAccount;
    private final MoneyTransfer moneyTransfer;
    private final AccountCreator accountCreator;
    private final AccountCRUDUpdater accountCRUDUpdater;
    private final Deposit deposit;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public void transferMoney(final MoneyTransfer.Command message) {
        moneyTransfer.transferMoney(message);
    }

    @Override
    @Transactional
    public void update(AccountDto dto) {
        accountCRUDUpdater.update(dto);
    }

    @Override
    @Transactional
    public AccountAccountNumber createAccount(final AccountCreator.Command message) {
        return accountCreator.createAccount(message);
    }

    @Override
    @Transactional
    public void deposit(final Deposit.Command message) {
        deposit.deposit(message);
    }

    //region Queries applying soft CQRS, we can skip service layer and access directly to repository
    //I think this should be moved to another facade
    @Override
    public AccountDto findOne(final String accountNumber) {
        return new AccountDto(repository.findOneOrElseThrow(accountNumber));
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
    public Optional<AccountDto> findByUserPassword(final String password) {
        return searchAccount.findByUserPassword(password);
    }

    @Override
    public List<AccountDto> searchForXXXPage(final Criteria filters) {
        return searchAccount.searchForXXXPage(filters);
    }
    //endregion
}
