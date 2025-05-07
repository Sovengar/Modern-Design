package jonathan.modern_design.account_module.application;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jonathan.modern_design._common.tags.ApplicationService;
import jonathan.modern_design._common.tags.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountNumber;
import jonathan.modern_design.account_module.domain.store.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static jonathan.modern_design._common.TraceIdGenerator.generateTraceId;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/api/v1/accounts")
class UpdateAccountCRUDHttpController {
    private final UpdateAccountCRUD updater;

    @Observed(name = "updateAccount")
    @Operation(description = "Update Account")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateAccount(@RequestBody AccountDto dto) {
        generateTraceId();

        log.info("BEGIN Updating account with number {} with this JSON: {}", dto.accountNumber(), dto);
        updater.handle(dto);
        log.info("END Account with number {} updated", dto.accountNumber());

        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class UpdateAccountCRUD {
    private final AccountRepo repository;

    /**
     * CRUD method, prefer usecase methods like moveToAnotherPlace to update the address only.
     * If we keep making the method more generic, the method will grow complex.
     * Logic will be dispersed since the client now has the burden to provide the right fields to support his need.
     */
    @Transactional
    public void handle(AccountDto dto) {
        log.info("BEGIN Update");

        var account = repository.findByAccNumberOrElseThrow(dto.accountNumber());
        account.updateCRUD(
                AccountNumber.of(dto.accountNumber()),
                AccountMoney.of(dto.balance(), Currency.valueOf(dto.currency())),
                AccountAddress.of(dto.address()),
                dto.status(),
                dto.userId()
        );

        repository.update(account);

        log.info("END Update");
    }
}
