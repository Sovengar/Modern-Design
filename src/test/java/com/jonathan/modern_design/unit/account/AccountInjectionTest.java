package com.jonathan.modern_design.unit.account;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.exceptions.InsufficientFundsException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.infraestructure.context.AccountConfigurationFactory;
import com.jonathan.modern_design.account_module.infraestructure.persistence.InMemoryAccountRepository;
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
    void should_send_money_into_the_target_account() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountEmpty();

        repository.create(source);
        repository.create(target);

        accountFacade.sendMoney(transactionWithAmount(50.0));

        assertThat(target.getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void approval_test_send_money_into_the_target_account() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountEmpty();

        repository.create(source);
        repository.create(target);

        accountFacade.sendMoney(transactionWithAmount(50.0));
        Approvals.verify(target.getAmount());
    }

    @Test
    void should_fail_when_sending_money_without_enough_money() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.sourceAccountEmpty();
        Account target = AccountStub.targetAccountwithBalance(100.0);

        repository.create(source);
        repository.create(target);

        assertThrows(InsufficientFundsException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }

    @Test
    void should_fail_when_source_account_is_inactive() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.sourceAccountInactive();
        Account target = AccountStub.targetAccountEmpty();

        repository.create(source);
        repository.create(target);

        assertThrows(AccountIsInactiveException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }

    @Test
    void should_fail_when_target_account_is_inactive() {
        accountFacade = new AccountConfigurationFactory().accountFacade(repository);

        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountInactive();

        repository.create(source);
        repository.create(target);

        assertThrows(AccountIsInactiveException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }
}
