package jonathan.modern_design.account_module.infra;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.application.AccountCreator;
import jonathan.modern_design.account_module.application.MoneyTransfer;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.CreateAccountCommand;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
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
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public void transferMoney(final TransferMoneyCommand command) {
        log.info("BEGIN TransferMoney");
        moneyTransfer.transferMoney(command);
        log.info("END TransferMoney");
    }

    //CRUD-Like method, prefer usecase methods like moveToAnotherPlace to update the address
    //If update ends up with more logic, extract to a service and make other services depend on it, i.e. transferMoney
    @Override
    @Transactional
    public void update(AccountDto dto) {
        log.info("BEGIN Update");
        var account = repository.findOneOrElseThrow(dto.accountNumber());
        account = Account.updateCRUD(
                account,
                AccountAccountNumber.of(dto.accountNumber()),
                AccountMoney.of(dto.balance(), Currency.valueOf(dto.currency())),
                AccountAddress.of(dto.address()),
                dto.active(),
                dto.userId());
        repository.update(account);

        log.info("END Update");
    }

    @Override
    @Transactional
    public AccountAccountNumber createAccount(final CreateAccountCommand command) {
        return accountCreator.createAccount(command);
    }

    @Override
    @Transactional
    public void deposit(final DepositCommand command) {
        log.info("BEGIN Deposit");
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        repository.update(account);
        log.info("END Deposit");
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
