package jonathan.modern_design.banking._dev_testing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jonathan.modern_design.__config.initializers.InfraInitializer;
import jonathan.modern_design.__config.runners.DatabaseRunner;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.models.QAccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static jonathan.modern_design.banking.domain.AccountDsl.givenAnEmptyAccount;

//NOT WORKING
@Slf4j
@DatabaseRunner
@DataJpaTest
@InfraInitializer
class QueryPlayGround {
    @Autowired
    private EntityManagerFactory emf;
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        queryFactory = new JPAQueryFactory(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        em.getTransaction().rollback();
        em.clear();
    }

    @Test
    void shouldFindAccountByAccountNumber() {
        // Arrange
        var accountEntity = new AccountEntity(givenAnEmptyAccount());

        em.persist(accountEntity);
        em.flush();
        em.clear();

        var account = queryFactory.selectFrom(QAccountEntity.accountEntity)
                .where(QAccountEntity.accountEntity.accountNumber.eq(accountEntity.getAccountNumber()))
                .fetchOne();

        var a = account;
    }
}
