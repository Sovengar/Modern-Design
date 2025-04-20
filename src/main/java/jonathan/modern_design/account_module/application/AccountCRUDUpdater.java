package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.AccountJdbcEntity;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.infra.AccountDto;
import jonathan.modern_design.account_module.infra.AccountRepoSpringDataJDBC;
import jonathan.modern_design.account_module.infra.AccountRepoSpringDataJPA;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Injectable
public class AccountCRUDUpdater {
    private final Storer repository;

    //CRUD-Like method, prefer usecase methods like moveToAnotherPlace to update the address
    @Transactional
    public void handle(AccountDto dto) {
        log.info("BEGIN Update");
//        var account = repository.findOneJdbc(dto.accountNumber()).orElseThrow();
//        account = Account.updateCRUD(
//                account,
//                AccountAccountNumber.of(dto.accountNumber()),
//                AccountMoney.of(dto.balance(), Currency.valueOf(dto.currency())),
//                AccountAddress.of(dto.address()),
//                dto.active(),
//                dto.userId());
//        repository.updateWithJdbc(account);

        var account = repository.findOneWithJpa(dto.accountNumber()).orElseThrow();
        account = Account.updateCRUD(
                account,
                AccountAccountNumber.of(dto.accountNumber()),
                AccountMoney.of(dto.balance(), Currency.valueOf(dto.currency())),
                AccountAddress.of(dto.address()),
                dto.active(),
                dto.userId());
        repository.updateWithJPA(account);

        log.info("END Update");
    }

    @DataAdapter
    @RequiredArgsConstructor
    public static class Storer {
        private final AccountRepoSpringDataJDBC repositoryJDBC;
        private final AccountRepoSpringDataJPA repositoryJPA;

        public void updateWithJdbc(final Account account) {
            var accountEntity = repositoryJDBC.findByAccountNumber(account.accountAccountNumber().accountNumber()).orElseThrow();
            accountEntity = new AccountJdbcEntity(accountEntity, account);
            repositoryJDBC.save(accountEntity);
        }

        public Optional<Account> findOneJdbc(final @NonNull String accountNumber) {
            var accountEntity = repositoryJDBC.findByAccountNumber(accountNumber);
            return accountEntity.map(AccountJdbcEntity::toDomain);
        }

        public void updateWithJPA(final Account account) {
            var accountEntity = repositoryJPA.findByAccountNumber(account.accountAccountNumber().accountNumber()).orElseThrow();
            Mapper.updateEntity(accountEntity, account);
            repositoryJPA.save(accountEntity);
        }

        public Optional<Account> findOneWithJpa(final String accountNumber) {
            var accountEntity = repositoryJPA.findByAccountNumber(accountNumber);
            return accountEntity.map(AccountEntity::toDomain);
        }
    }
}

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class AccountCRUDUpdaterController {
    private final AccountCRUDUpdater updater;

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public void updateAccount(@RequestBody AccountDto dto) {
        updater.handle(dto);
    }
}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Mapper {
    public static void updateEntity(AccountEntity accountEntity, Account account) {
        accountEntity.accountNumber(account.accountAccountNumber().accountNumber());
        accountEntity.balance(account.money().amount());
        accountEntity.currency(account.money().currency());
        accountEntity.address(account.address().toString());
        accountEntity.userId(account.userId());
        accountEntity.dateOfLastTransaction(account.dateOfLastTransaction());
        accountEntity.active(account.active());
    }
}
