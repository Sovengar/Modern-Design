package jonathan.modern_design.account_module.infra.store;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

interface AccountProjection {
    Long getAccountId();

    String getAccountNumber();

    BigDecimal getBalance();

    String getCurrency();
}

@SpringBootTest
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //Care with shared state
class AccountRepoSpringDataJPATest extends ITConfig {
    @Autowired
    private AccountRepoSpringDataJPA accountRepository;

    @BeforeEach
    void setUp() {
        AccountEntity entity = new AccountEntity();
        entity.accountNumber("ES123456789");
        entity.balance(new BigDecimal("1500.00"));
        entity.currency(Currency.EUR);
        entity.address("C/ Passeig de Gràcia 42");
        entity.dateOfLastTransaction(LocalDateTime.now().minusDays(1));
        entity.userId(null);
        entity.active(true);

        accountRepository.save(entity);
    }

    @Test
    void testDynamicProjection() {
        List<AccountProjection> projectionList = accountRepository.findByAccountNumber("ES123456789", AccountProjection.class);
        List<AccountEntity> entityList = accountRepository.findByAccountNumber("ES123456789", AccountEntity.class);
        //List<AccountDto> dtoList = accountRepository.findByAccountNumber("ES123456789", AccountDto.class);
        //Cannot set field 'currency' to instantiate 'jonathan.modern_design.account_module.api.dtos.AccountDto'
        //List<Account> domainModelList = accountRepository.findByAccountNumber("ES123456789", Account.class);

        assertThat(projectionList).isEqualTo(1);
        assertThat(entityList).isEqualTo(1);
        //assertThat(dtoList).isEqualTo(1);
        //assertThat(domainModelList).isEqualTo(1);

        projectionList.forEach(projection -> {
            log.info("Projection -> id: {}, number: {}, balance: {}, currency: {}",
                    projection.getAccountId(),
                    projection.getAccountNumber(),
                    projection.getBalance(),
                    projection.getCurrency()
            );
        });
    }
}
