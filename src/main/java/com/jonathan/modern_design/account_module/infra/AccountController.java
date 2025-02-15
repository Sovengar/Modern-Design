package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.create_account.CreateAccountCommand;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.config.annotations.WebAdapter;
import com.jonathan.modern_design.shared.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountFacade accountFacade;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{amount}/{currency}")
    void transferMoney(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("currency") String currency) {

        log.info("BEGIN Transfer money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);

        val command = new SendMoneyCommand(sourceAccountId, targetAccountId, amount, Currency.fromCode(currency));

        accountFacade.sendMoney(command);

        log.info("END Transfer money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);
    }

    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountResource> loadAccount(@PathVariable String accountNumber) {
        val account = accountFacade.findOne(accountNumber).orElseThrow();
        return ok(new AccountResource(account));
    }

    @PostMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountResource> createAccount(@RequestBody CreateAccountCommand createAccountCommand) {
        log.info("START - Create account");
        final var account = accountFacade.createAccount(createAccountCommand);
        final var accountNumber = account.getAccountNumber();
        log.info("END - Create account: {}", accountNumber);

        var build = fromMethodCall(on(this.getClass()).loadAccount(accountNumber)).build();
        return created(build.toUri()).body(new AccountResource(account));
    }


}
