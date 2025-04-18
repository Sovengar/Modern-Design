package jonathan.modern_design.account_module.application;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.infra.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static jonathan.modern_design.account_module.infra.QAccountEntity.accountEntity;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class GetAccount {
    private final GetAccountQuery querier;

    @GetMapping(path = "/{accountNumber}")
    ResponseEntity<AccountDto> loadAccount(@PathVariable String accountNumber) {
        return ok(querier.getAccount(accountNumber));
    }
}

@Query
@RequiredArgsConstructor
class GetAccountQuery {
    @PersistenceContext
    private final EntityManager entityManager;

    public AccountDto getAccount(final String accountNumber) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        var account = queryFactory.selectFrom(accountEntity)
                .where(accountEntity.accountNumber.eq(accountNumber))
                .fetchOne();

        assert account != null;
        return new AccountDto(account);
    }
}
