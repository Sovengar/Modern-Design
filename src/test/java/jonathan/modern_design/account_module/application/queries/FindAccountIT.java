package jonathan.modern_design.account_module.application.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.account_module.domain.models.account.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class FindAccountIT extends ITConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FindAccount findAccount;

    @Test
    void shouldFindAccountByAccountNumber() {
        AccountEntity account = AccountEntity.builder()
                .accountNumber("ACC123")
                .balance(BigDecimal.ZERO)
                .build();

        entityManager.persist(account);
        entityManager.flush();
        entityManager.clear();

        // Act
        //entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        var acc1 = entityManager.createNativeQuery("SELECT * FROM account").getFirstResult();
        var acc2 = entityManager.find(AccountEntity.class, "ACC123");
        var acc3 = findAccount.queryWith("ACC123");

        entityManager.getTransaction().commit();
        entityManager.close();

        // Assert
        assertThat(acc3.accountNumber()).isEqualTo("ACC123");
    }

    @Test
    void shouldFailIfAccountDoesNotExist() {
        assertThrows(AssertionError.class, () -> findAccount.queryWith("NOT_FOUND"));
    }
}
