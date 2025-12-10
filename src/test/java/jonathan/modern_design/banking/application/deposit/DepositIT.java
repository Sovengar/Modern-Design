package jonathan.modern_design.banking.application.deposit;

import jonathan.modern_design.__config.shared_for_all_classes.AcceptanceTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._shared.domain.catalogs.Currency;
import jonathan.modern_design.banking.BankingDsl;
import jonathan.modern_design.banking.application.Deposit;
import jonathan.modern_design.banking.domain.store.AccountRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static jonathan.modern_design.banking.AccountStub.DEFAULT_ACCOUNT_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AcceptanceTest
@EnableTestContainers
class DepositIT extends BankingDsl {
    @Autowired
    private Deposit should;

    @Autowired
    private AccountRepo repository;

    @Test
    void should_deposit_funds_successfully() {
        givenAnEmptyAccount();

        should.handle(new Deposit.Command(DEFAULT_ACCOUNT_NUMBER, BigDecimal.TEN, Currency.EUR));

        var fetchedAccount = repository.findByAccNumberOrElseThrow(DEFAULT_ACCOUNT_NUMBER);
        assertThat(fetchedAccount.getMoney().getBalance()).isEqualTo(BigDecimal.TEN);
    }
}
