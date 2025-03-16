package jonathan.modern_design.account_module.infra;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.AccountApi;
import jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class AccountController {
    private final AccountApi accountFacade;

    @Transactional
    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{balance}/{currency}")
    void transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("balance") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);

        val command = new TransferMoneyCommand(sourceAccountId, targetAccountId, amount, Currency.fromDesc(currency));

        accountFacade.transferMoney(command);

        log.info("END Transfer money from {} to {} with balance {}", sourceAccountId, targetAccountId, amount);
    }

    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountDto> loadAccount(@PathVariable String accountNumber) {
        return ok(accountFacade.findOne(accountNumber));
    }

    //@Operation(description = "Search Account")
    @PostMapping("/search")
    public List<AccountDto> search(@RequestBody AccountSearchRepo.AccountSearchCriteria searchCriteria) {
        return accountFacade.search(searchCriteria);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    //OPENAPI @Operation(description = "Create Account")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountCreatorCommand accountCreatorCommand) {
        log.info("START - Create account");
        final var accountNumber = accountFacade.createAccount(accountCreatorCommand).getValue();
        log.info("END - Created account: {}", accountNumber);

        var uri = fromMethodCall(on(this.getClass()).loadAccount(accountNumber)).build().toUri();
        return ResponseEntity.created(uri).body(accountFacade.findOne(accountNumber));
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public void updateAccount(@RequestBody AccountDto dto) {
        accountFacade.update(dto);
    }

    @GetMapping(path = "/{accountNumber}/balance")
    public BigDecimal getBalance(@PathVariable String accountNumber) {
        return accountFacade.findOne(accountNumber).balance();
    }

    @GetMapping(path = "/{password}/user")
    public AccountDto getAcc(@PathVariable String password) {
        return accountFacade.findByUserPassword(password).orElseThrow(EntityNotFoundException::new);
    }
}
