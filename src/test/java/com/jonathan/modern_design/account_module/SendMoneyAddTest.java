package com.jonathan.modern_design.account_module;

import com.jonathan.modern_design.account_module.application.AccountFacade;
import com.jonathan.modern_design.account_module.domain.AccountRepository;
import com.jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountMoneyVO;
import com.jonathan.modern_design.account_module.infraestructure.context.AccountConfigurationFactory;
import com.jonathan.modern_design.account_module.infraestructure.persistence.AccountRepositoryFake;
import com.jonathan.modern_design.config.ArticleDsl;
import com.jonathan.modern_design.fake_data.AccountStub;
import com.jonathan.modern_design.user_module.UserFacade;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import static com.jonathan.modern_design.fake_data.SendMoneyMother.transactionWithAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SendMoneyAddTest extends ArticleDsl {

    @Mock private UserFacade userFacade;
    private final AccountRepository repository = new AccountRepositoryFake();
    private final AccountFacade accountFacade = new AccountConfigurationFactory().accountFacade(repository, userFacade);

    private void poblatePersistenceLayer(Account source, Account target) {
        repository.create(source);
        repository.create(target);
    }

    @Test
    void should_send_money_into_the_target_account() {
        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountEmpty();
        poblatePersistenceLayer(source, target);

        accountFacade.sendMoney(transactionWithAmount(50.0));

        assertThat(target.getAmount()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void approval_test_send_money_into_the_target_account() {
        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountEmpty();
        poblatePersistenceLayer(source, target);

        accountFacade.sendMoney(transactionWithAmount(50.0));
        Approvals.verify(target.getAmount());
    }

    @Test
    void should_fail_when_sending_money_without_enough_money() {
        Account source = AccountStub.sourceAccountEmpty();
        Account target = AccountStub.targetAccountwithBalance(100.0);
        poblatePersistenceLayer(source, target);

        assertThrows(AccountMoneyVO.InsufficientFundsException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }

    @Test
    void should_fail_when_source_account_is_inactive() {
        Account source = AccountStub.sourceAccountInactive();
        Account target = AccountStub.targetAccountEmpty();
        poblatePersistenceLayer(source, target);

        assertThrows(AccountIsInactiveException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }

    @Test
    void should_fail_when_target_account_is_inactive() {
        Account source = AccountStub.sourceAccountwithBalance(100.0);
        Account target = AccountStub.targetAccountInactive();
        poblatePersistenceLayer(source, target);

        assertThrows(AccountIsInactiveException.class, () -> {
            accountFacade.sendMoney(transactionWithAmount(50.0));
        });
    }
}
