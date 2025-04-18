package jonathan.modern_design.account_module.application;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.Repo;
import jonathan.modern_design._common.annotations.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

import static jonathan.modern_design.account_module.infra.QAccountEntity.accountEntity;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class GetBalance {
    private final GetBalanceQuery getBalanceQuery;

    @GetMapping(path = "/{accountNumber}/balance")
    public BigDecimal getBalance(@PathVariable String accountNumber) {
        return getBalanceQuery.getBalance(accountNumber);
    }
}

@Repo
@RequiredArgsConstructor
class GetBalanceQuery {
    @PersistenceContext
    private final EntityManager entityManager;

    public BigDecimal getBalance(final String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        return queryFactory
                .select(accountEntity.balance)
                .from(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();
    }
}
