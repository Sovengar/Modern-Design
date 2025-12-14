package jonathan.modern_design.banking.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design.__config.details.IntegrationTags;
import jonathan.modern_design.__config.runners.DatabaseITRunner;
import jonathan.modern_design.__config.utils.EnableTestContainers;
import jonathan.modern_design.banking.BankingDsl;
import jonathan.modern_design.banking.domain.AccountDsl;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static jonathan.modern_design.banking.domain.AccountDsl.givenAnAccountWithUserId;
import static org.assertj.core.api.Assertions.assertThat;

@DatabaseITRunner
@IntegrationTags
@EnableTestContainers
@Import(FindAccount.class)
class FindAccountIT extends BankingDsl {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FindAccount findAccount;

    @Test
    void shouldFindAccountByAccountNumber() {
        givenAnEmptyAccount();
        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);

        var accountDto = findAccount.queryWith(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER).orElseThrow();

        assertThat(accountDto.accountNumber()).isEqualTo(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER);
    }

    @Test
    void shouldFindAccountByUserId() {
        // Arrange
        var userId = UUID.randomUUID();
        var accountEntity = new AccountEntity(givenAnAccountWithUserId(userId));
        givenARandomAccountWithBalance(10.0);
        givenARandomAccountWithBalance(10.0);

        //entityManager.persist(accountHolder);
        entityManager.persist(accountEntity);
        entityManager.flush();
        entityManager.clear();

        // Act
        var result = findAccount.queryWithUserId(userId).orElseThrow();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER);
    }
}
