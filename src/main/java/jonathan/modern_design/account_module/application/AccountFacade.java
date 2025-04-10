package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Inyectable;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountNumber;
import jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.DepositCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import jonathan.modern_design.account_module.infra.AccountMapper;
import jonathan.modern_design.account_module.infra.AccountSearchRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Inyectable
@RequiredArgsConstructor
@Slf4j
public class AccountFacade implements AccountApi {
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

    @Override
    @Transactional
    public void update(AccountDto dto) {
        log.info("BEGIN Update");
        var account = accountMapper.toAccount(dto);
        update(account);
        log.info("END Update");
    }

    @Override
    @Transactional
    public AccountNumber createAccount(final AccountCreatorCommand command) {
        log.info("BEGIN CreateAccount");
        var accountNumber = accountCreator.createAccount(command);

        log.info("END CreateAccount");
        return accountNumber;
    }

    @Override
    @Transactional
    public void deposit(final DepositCommand command) {
        log.info("BEGIN Deposit");
        var account = repository.findOne(command.accountNumber()).orElseThrow();
        account.add(command.amount(), command.currency());
        update(account);
        log.info("END Deposit");
    }

    private void update(Account account) {
        //If update ends up with more logic, extract to a service and make other services depend on it, i.e. transferMoney
        repository.update(account);
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
