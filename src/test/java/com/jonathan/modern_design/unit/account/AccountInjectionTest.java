package com.jonathan.modern_design.unit.account;

import com.jonathan.modern_design.account.application.AccountFacade;
import com.jonathan.modern_design.account.domain.AccountRepository;
import com.jonathan.modern_design.account.domain.exceptions.InsufficientFundsException;
import com.jonathan.modern_design.account.domain.model.Account;
import com.jonathan.modern_design.account.infraestructure.context.AccountConfigurationFactory;
import com.jonathan.modern_design.account.infraestructure.persistence.InMemoryAccountRepository;
import com.jonathan.modern_design.fake_data.AccountStub;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.jonathan.modern_design.fake_data.SendMoneyCommandMother.transactionWithAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountInjectionTest extends ArticleDsl {
    private final AccountRepository repository = new InMemoryAccountRepository();
    private AccountFacade accountFacade;

    @Test
    void should_inject_money_into_the_account() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.withBalance(1L, 100.0);
        Account target = AccountStub.emptyAccount(2L);

        repository.create(source);
        repository.create(target);

        accountFacade.sendMoney(transactionWithAmount(50.0));

        assertThat(target.getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void approval_test_inject_money_into_the_account() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.withBalance(1L, 100.0);
        Account target = AccountStub.emptyAccount(2L);

        repository.create(source);
        repository.create(target);

        accountFacade.sendMoney(transactionWithAmount(50.0));
        Approvals.verify(target.getAmount());
    }

    @Test
    void should_not_inject_money_into_the_account() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.emptyAccount(1L);
        Account target = AccountStub.withBalance(2L, 100.0);

        repository.create(source);
        repository.create(target);

        assertThrows(InsufficientFundsException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));  // Supongamos que el source no tiene suficiente dinero
        });
    }
}
