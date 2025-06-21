package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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

    AccountEntity givenAnAccount() {
        var acc = AccountEntity.Factory.create(null, "ES123456789", new BigDecimal("1500.00"), Currency.EUR, "C/ Paseo de Gràcia 42", null);
        accountRepository.save(acc);
        return acc;
    }

    @Test
    void testDynamicProjection() {
        givenAnAccount();

        //When
        List<AccountProjection> projectionList = accountRepository.findByAccountNumber("ES123456789", AccountProjection.class);
        List<AccountEntity> entityList = accountRepository.findByAccountNumber("ES123456789", AccountEntity.class);
        //List<AccountDto> dtoList = accountRepository.findByAccountNumber("ES123456789", AccountDto.class);
        //Cannot set field 'currency' to instantiate 'jonathan.modern_design.banking.api.dtos.AccountDto'
        //List<Account> domainModelList = accountRepository.findByAccountNumber("ES123456789", Account.class);

        //Then
        assertThat(projectionList).hasSize(1);
        assertThat(entityList).hasSize(1);
        //assertThat(dtoList).isEqualTo(1);
        //assertThat(domainModelList).isEqualTo(1);

        projectionList.forEach(projection -> log.info("Projection -> id: {}, number: {}, balance: {}, currency: {}",
                projection.getAccountId(),
                projection.getAccountNumber(),
                projection.getBalance(),
                projection.getCurrency()
        ));
    }
}

