package com.jonathan.modern_design._infra.web;

import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.AccountFacade;
import com.jonathan.modern_design.account_module.application.AccountResource;
import com.jonathan.modern_design.account_module.application.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.infra.query.AccountSearchCriteria;
import jakarta.transaction.Transactional;
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
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
class AccountController {
    private final AccountFacade accountFacade;

    @Transactional
    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{amount}/{currency}")
    void transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Transfer money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);

        val command = new TransferMoneyUseCase.TransferMoneyCommand(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        accountFacade.transferMoney(command);

        log.info("END Transfer money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);
    }

    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountResource> loadAccount(@PathVariable String accountNumber) {
        return ok(accountFacade.findOne(accountNumber));
    }

    //@Operation(description = "Search Account")
    @PostMapping("/search")
    public List<AccountResource> search(@RequestBody AccountSearchCriteria searchCriteria) {
        return accountFacade.search(searchCriteria);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    //OPENAPI @Operation(description = "Create Account")
    public ResponseEntity<AccountResource> createAccount(@RequestBody CreateAccountUseCase.CreateAccountCommand createAccountCommand) {
        log.info("START - Create account");
        final var accountNumber = accountFacade.createAccount(createAccountCommand).getValue();
        log.info("END - Created account: {}", accountNumber);

        return on(this.getClass()).loadAccount(accountNumber);
        //var uri = fromMethodCall(on(this.getClass()).loadAccount(accountNumber)).build().toUri();
        //return created(uri).body(new AccountResource(account));
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public void updateAccount(@RequestBody AccountResource dto) {
        accountFacade.update(dto);
    }

}
