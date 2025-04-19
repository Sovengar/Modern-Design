package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.Repo;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.AccountEntity;
import jonathan.modern_design.account_module.domain.FindAccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.infra.AccountDto;
import jonathan.modern_design.account_module.infra.AccountRepoSpringDataJDBC;
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

    @Repo
    @RequiredArgsConstructor
    public static class Storer implements FindAccountRepo {
        private final AccountRepoSpringDataJDBC repository;

        public void update(final Account account) {
            var accountEntity = findOneEntityOrElseThrow(account.accountAccountNumber().accountNumber());
            Mapper.updateEntity(accountEntity, account);
            repository.save(accountEntity);
        }

        @Override
        public Optional<Account> findOne(final String accountNumber) {
            var accountEntity = findOneEntity(accountNumber);
            return accountEntity.map(AccountEntity::toDomain);
        }

        @Override
        public Optional<AccountEntity> findOneEntity(final @NonNull String accountNumber) {
            return repository.findByAccountNumber(accountNumber);
        }
    }
}

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class Controller {
    private final AccountCRUDUpdater updater;

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public void updateAccount(@RequestBody AccountDto dto) {
        updater.update(dto);
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
