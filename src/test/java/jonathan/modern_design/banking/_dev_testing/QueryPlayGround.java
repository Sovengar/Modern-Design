package jonathan.modern_design.banking._dev_testing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jonathan.modern_design.__config.IntegrationConfig;
import jonathan.modern_design.__config.shared_for_all_classes.DatabaseTest;
import jonathan.modern_design.__config.shared_for_all_classes.EnableTestContainers;
import jonathan.modern_design._dsl.AccountStub;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.models.QAccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//NOT WORKING
@Slf4j
@DatabaseTest
@IntegrationConfig
@EnableTestContainers
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
        var accountEntity = new AccountEntity(AccountStub.AccountMother.emptyAccount());

        em.persist(accountEntity);
        em.flush();
        em.clear();

        var account = queryFactory.selectFrom(QAccountEntity.accountEntity)
                .where(QAccountEntity.accountEntity.accountNumber.eq(accountEntity.getAccountNumber()))
                .fetchOne();

        var a = account;
    }
}
