package jonathan.modern_design.banking.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.shared_for_all_classes.DatabaseTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design.banking.BankingDsl;
import jonathan.modern_design.banking.domain.AccountStub;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@DatabaseTest
@IntegrationConfig
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

        var accountDto = findAccount.queryWith(AccountStub.DEFAULT_SOURCE_ACCOUNT_NUMBER).orElseThrow();

        assertThat(accountDto.accountNumber()).isEqualTo(AccountStub.DEFAULT_SOURCE_ACCOUNT_NUMBER);
    }

    @Test
    void shouldFindAccountByUserId() {
        // Arrange
        var userId = UUID.randomUUID();
        var accountEntity = new AccountEntity(AccountStub.AccountMother.accountWithUserId(userId));
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
        assertThat(result.accountNumber()).isEqualTo(AccountStub.DEFAULT_SOURCE_ACCOUNT_NUMBER);
    }

    @Test
    void shouldFailIfAccountDoesNotExist() {
        assertThrows(AssertionError.class, () -> findAccount.queryWith("NOT_FOUND"));
    }
}
