package com.jonathan.modern_design.unit;

import com.jonathan.modern_design.Dsl;
import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.infraestructure.context.AccountConfigurationFactory;
import com.jonathan.modern_design.fake_data.AccountStub;
import com.jonathan.modern_design.fake_data.SendMoneyCommandMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest extends Dsl {

    private final AccountFacade accountFacade = AccountConfigurationFactory.createDefault();

    @Test
    void should_inject_money_into_the_account() {
        final var moneySent = accountFacade.send(SendMoneyCommandMother.validTransaction());
        assertThat(moneySent).isTrue();
    }
}
