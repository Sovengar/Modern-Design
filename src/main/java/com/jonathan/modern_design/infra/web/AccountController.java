package com.jonathan.modern_design.infra.web;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.application.create_account.AccountDataCommand;
import com.jonathan.modern_design.account_module.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.shared.annotations.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountFacade accountFacade;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{amount}")
    void transfer(
            @PathVariable("sourceAccountId") String sourceAccountId,
            @PathVariable("targetAccountId") String targetAccountId,
            @PathVariable("amount") BigDecimal amount) {

        log.info("Sending money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);

        final var command = SendMoneyCommand.builder()
                .sourceId(sourceAccountId)
                .targetId(targetAccountId)
                .amount(amount).build();

        accountFacade.sendMoney(command);
    }

    @GetMapping(path = "/load/{accountNumber}")
    void load(@PathVariable String accountNumber) {
        accountFacade.findOne(accountNumber);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody AccountDataCommand accountDataCommand) {
        log.info("START - Create account");

        final var account = accountFacade.createAccount(accountDataCommand);
        final var accountNumber = account.getAccountNumber();

        log.info("END - Create account: {}", accountNumber);
        return accountNumber;
    }
}
