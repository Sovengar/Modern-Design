package com.jonathan.modern_design.unit.account;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.domain.Account;
import com.jonathan.modern_design.account.infraestructure.context.AccountConfiguration;
import com.jonathan.modern_design.account.infraestructure.persistence.InMemoryAccountRepository;
import com.jonathan.modern_design.fake_data.AccountStub;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static com.jonathan.modern_design.fake_data.SendMoneyCommandMother.validTransaction;


class AccountTest extends ArticleDsl {

    private final AccountFacade accountFacade = new AccountConfiguration().accountFacade();

    @Test
    void should_inject_money_into_the_account() {
        InMemoryAccountRepository accountRepository = accountFacade.getRepository();

        Account account1 = AccountStub.withBalance(1L, 100.0);
        Account account2 = AccountStub.emptyAccount(2L);

        accountRepository.create(account1);
        accountRepository.create(account2);

        final var moneySent = accountFacade.sendMoney(validTransaction());
        assertThat(moneySent).isTrue();
    }
}
