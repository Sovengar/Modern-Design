package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.application.AccountCRUDUpdater;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.Deposit;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.dtos.AccountDto;
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
    private final AccountSearchRepo accountSearcher;
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
    @Override
    public AccountDto findOne(final String accountNumber) {
        log.debug("BEGIN FindOne");

        final var account = repository.findOneOrElseThrow(accountNumber);

        log.debug("END FindOne");
        return new AccountDto(account);
    }

    //I think this should be moved to another facade
    @Override
    public List<AccountDto> search(final AccountSearchCriteria filters) {
        log.info("BEGIN Search");
        var accounts = accountSearcher.search(filters);

        log.info("END Search");
        return accounts;
        //TODO Tener varios search por caso de uso y n mappers, n projections, ...
    }

    @Override
    public Optional<AccountDto> findByUserPassword(final String password) {
        return accountSearcher.findByUserPassword("password");
    }
    //endregion
}
