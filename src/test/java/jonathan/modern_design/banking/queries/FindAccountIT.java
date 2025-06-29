package jonathan.modern_design.banking.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.shared_for_all_classes.DatabaseTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.AccountStub;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@DatabaseTest
@IntegrationConfig
@EnableTestContainers
class FindAccountIT {
    @PersistenceContext
    private EntityManager entityManager;

    private FindAccount findAccount;

    @BeforeEach
    void setUp() {
        findAccount = new FindAccount(entityManager);
    }

    @Test
    void shouldFindAccountByAccountNumber() {
        var accountEntity = new AccountEntity(AccountStub.AccountMother.sourceAccountEmpty());

        entityManager.persist(accountEntity);
        entityManager.flush();
        entityManager.clear();

        // Act
        var accountDto = findAccount.queryWith(AccountStub.sourceAccountId);

        // Assert
        assertThat(accountDto.accountNumber()).isEqualTo(AccountStub.sourceAccountId);
    }

    @Test
    void shouldFindAccountByUserId() {
        // Arrange
        var userId = UUID.randomUUID();
        var accountEntity = new AccountEntity(AccountStub.AccountMother.accountWithUserId(userId));

        //entityManager.persist(accountHolder);
        entityManager.persist(accountEntity);
        entityManager.flush();
        entityManager.clear();

        // Act
        var result = findAccount.queryWithUserId(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo(AccountStub.sourceAccountId);
    }

    @Test
    void shouldFailIfAccountDoesNotExist() {
        assertThrows(AssertionError.class, () -> findAccount.queryWith("NOT_FOUND"));
    }
}
