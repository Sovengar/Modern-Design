package jonathan.modern_design.account_module.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.repos.AccountRepo;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.infra.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Injectable
public class AccountCRUDUpdater {
    private final AccountRepo repository;

    //CRUD-Like method, prefer usecase methods like moveToAnotherPlace to update the address
    @Transactional
    public void handle(AccountDto dto) {
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
