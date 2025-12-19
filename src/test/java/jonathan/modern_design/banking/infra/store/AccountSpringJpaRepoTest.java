package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.__config.initializers.InfraInitializer;
import jonathan.modern_design.__config.runners.DatabaseRunner;
import jonathan.modern_design.banking.BankingDsl;
import jonathan.modern_design.banking.domain.AccountDsl;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountSpringJpaRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

interface AccountProjection {
    Long getId();

    String getAccountNumber();

    BigDecimal getBalance();

    String getCurrency();
}

@Slf4j
@DatabaseRunner
@DataJpaTest
@InfraInitializer
class AccountSpringJpaRepoTest extends BankingDsl {
    @Autowired
    private AccountSpringJpaRepo accountRepository;

    @Test
    void testDynamicProjection() {
        givenAnEmptyAccount();

        //When
        List<AccountProjection> projectionList = accountRepository.findByAccountNumber(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER, AccountProjection.class);
        List<AccountEntity> entityList = accountRepository.findByAccountNumber(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER, AccountEntity.class);
        //List<AccountDto> dtoList = accountRepository.findByAccountNumber("ES123456789", AccountDto.class);
        //Cannot set field 'currency' to instantiate 'jonathan.modern_design.banking.api.dtos.AccountDto'
        //List<Account> domainModelList = accountRepository.findByAccountNumber("ES123456789", Account.class);

        //Then
        assertThat(projectionList).hasSize(1);
        assertThat(entityList).hasSize(1);
        //assertThat(dtoList).isEqualTo(1);
        //assertThat(domainModelList).isEqualTo(1);

        projectionList.forEach(projection -> log.info("Projection -> id: {}, number: {}, balance: {}, currency: {}",
                projection.getId(),
                projection.getAccountNumber(),
                projection.getBalance(),
                projection.getCurrency()
        ));
    }
}

