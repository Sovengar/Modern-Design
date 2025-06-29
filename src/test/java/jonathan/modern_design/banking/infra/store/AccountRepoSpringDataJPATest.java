package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.shared_for_all_classes.DatabaseTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.AccountStub;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.infra.store.spring.AccountRepoSpringDataJPA;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static jonathan.modern_design._dsl.BankingDsl.givenAnAccount;
import static org.assertj.core.api.Assertions.assertThat;

interface AccountProjection {
    Long getId();

    String getAccountNumber();

    BigDecimal getBalance();

    String getCurrency();
}

@Slf4j
@DatabaseTest
@IntegrationConfig
@EnableTestContainers
class AccountRepoSpringDataJPATest {
    @Autowired
    private AccountRepoSpringDataJPA accountRepository;


    @Test
    void testDynamicProjection() {
        givenAnAccount(accountRepository);

        //When
        List<AccountProjection> projectionList = accountRepository.findByAccountNumber(AccountStub.sourceAccountId, AccountProjection.class);
        List<AccountEntity> entityList = accountRepository.findByAccountNumber(AccountStub.sourceAccountId, AccountEntity.class);
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

