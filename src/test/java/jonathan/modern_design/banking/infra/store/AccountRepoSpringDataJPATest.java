package jonathan.modern_design.banking.infra.store;

import jonathan.modern_design.__config.shared_for_all_tests_in_class.ITConfig;
import jonathan.modern_design._shared.domain.Currency;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import jonathan.modern_design.banking.infra.store.spring.AccountRepoSpringDataJPA;
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
        var account = Account.Factory.create(AccountNumber.of("ES123456789"), Money.of(new BigDecimal("1500.00"), Currency.EUR), null);
        var accountEntity = new AccountEntity(account);
        accountRepository.save(accountEntity);
        return accountEntity;
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

