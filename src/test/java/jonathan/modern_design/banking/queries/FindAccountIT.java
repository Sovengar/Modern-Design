package jonathan.modern_design.banking.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FindAccountIT {
    private EntityManagerFactory emf;
    private EntityManager em;
    private FindAccount findAccount;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        findAccount = new FindAccount(em);
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    void shouldFindAccountByAccountNumber() {
        // Arrange
        var accountHolder = AccountHolder.create(UUID.randomUUID(), Optional.of("John Doe"), null, null, null, null, null);

        var account = AccountEntity.builder()
                .accountNumber("ACC123")
                .balance(BigDecimal.ZERO)
                .accountHolder(accountHolder)
                .build();

        em.persist(accountHolder);
        em.persist(account);
        em.flush();
        em.clear();

        // Act
        var result = findAccount.queryWith("ACC123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo("ACC123");

        em.getTransaction().rollback();
    }

    @Test
    void shouldFindAccountByUserId() {
        // Arrange
        var userId = UUID.randomUUID();
        var accountHolder = AccountHolder.create(UUID.randomUUID(), Optional.of("John Doe"), null, null, null, null, userId);

        var account = AccountEntity.builder()
                .accountNumber("ACC123")
                .balance(BigDecimal.ZERO)
                .accountHolder(accountHolder)
                .build();

        em.persist(accountHolder);
        em.persist(account);
        em.flush();
        em.clear();

        // Act
        var result = findAccount.queryWithUserId(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo("ACC123");

        em.getTransaction().rollback();
    }


}
