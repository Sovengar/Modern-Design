package com.jonathan.modern_design.web;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.application.send_money.SendMoneyCommand;
import com.jonathan.modern_design.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class AccountController {
    private final AccountFacade accountFacade;

    @PostMapping(path = "/transfer/{sourceAccountId}/{targetAccountId}/{amount}")
    void transfer(
            @PathVariable("sourceAccountId") UUID sourceAccountId,
            @PathVariable("targetAccountId") UUID targetAccountId,
            @PathVariable("amount") BigDecimal amount) {

        log.info("Sending money from {} to {} with amount {}", sourceAccountId, targetAccountId, amount);

        final var command = SendMoneyCommand.builder()
                .sourceId(sourceAccountId)
                .targetId(targetAccountId)
                .amount(amount).build();

        accountFacade.sendMoney(command);
    }

    @GetMapping(path = "/load/{accountId}")
    void load(@PathVariable UUID accountId) {
        accountFacade.findOne(accountId);
    }
}
