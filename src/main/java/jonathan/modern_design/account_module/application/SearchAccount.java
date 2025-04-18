package jonathan.modern_design.account_module.application;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.Query;
import jonathan.modern_design._common.annotations.WebAdapter;
import jonathan.modern_design.account_module.infra.AccountDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static jonathan.modern_design.account_module.infra.QAccountEntity.accountEntity;
import static jonathan.modern_design.user.domain.QUser.user;

public interface SearchAccount {
    List<AccountSearchResult> searchWithJPQL(Criteria filters);

    List<AccountSearchResult> searchWithQueryDSL(Criteria filters);

    Optional<AccountDto> findByUserPassword(final String password);

    List<AccountDto> searchForXXXPage(Criteria filters);

    @Builder
    record Criteria(
            String username,
            String email,
            String countryCode
    ) {
    }

    record AccountSearchResult(Long accountId, String username) {
    }
}

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@RequestMapping("/api/v1/accounts")
class SearchAccountController {
    private final SearchAccountQueryImpl querier;

    //@Operation(description = "Search Account")
    @PostMapping("/search")
    public List<AccountDto> searchForXXXPage(@RequestBody SearchAccount.Criteria filters) {
        return querier.searchForXXXPage(filters);
    }

    @GetMapping(path = "/search/byuser/password/{password}")
    public AccountDto findAccount(@PathVariable String password) {
        return querier.findByUserPassword(password).orElseThrow(EntityNotFoundException::new);
    }

    //@Operation(description = "Search Account")
    @PostMapping("/search/xxxPage")
    public List<SearchAccount.AccountSearchResult> searchProjectionForXXXPage(@RequestBody SearchAccount.Criteria filters) {
        return querier.searchWithQueryDSL(filters);
    }
}

@Query
//Has to have Impl in the name to avoid Spring mapping to JPARepository
class SearchAccountQueryImpl implements SearchAccount {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public SearchAccountQueryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    @Override
    public List<AccountSearchResult> searchWithJPQL(Criteria filters) {
        // Alternative: Spring Specifications https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html
        String jpql = "SELECT new jonathan.modern_design.account_module.application.SearchAccount.AccountSearchResult(a.id, a.name)" +
                " FROM Account a " +
                " WHERE ";
        List<String> jpqlParts = new ArrayList<>();
        jpqlParts.add("1=1"); // alternatives: Criteria API ± Spring Specifications or Query DSL
        Map<String, Object> params = new HashMap<>();

        ofNullable(filters.username())
                .ifPresent(username -> {
                    jpqlParts.add("UPPER(c.username) LIKE UPPER('%' || :username || '%')");
                    params.put("username", username);
                });

        ofNullable(filters.email())
                .ifPresent(email -> {
                    jpqlParts.add("UPPER(c.email) = UPPER(:email)");
                    params.put("email", email);
                });

        ofNullable(filters.countryCode())
                .ifPresent(countryCode -> {
                    jpqlParts.add("c.country.code = :countryCode");
                    params.put("countryCode", countryCode);
                });

        String whereCriteria = join(" AND ", jpqlParts);
        var query = entityManager.createQuery(jpql + whereCriteria, SearchAccount.AccountSearchResult.class);
        for (var entry : params.entrySet()) {
            query.setParameter(entry.getKey(), params.get(entry.getKey()));
        }
        return query.getResultList();
    }

    @Override
    public List<AccountSearchResult> searchWithQueryDSL(Criteria filters) {
        var filtersBuilded = buildFilters(filters);

        return queryFactory
                .select(Projections.constructor(AccountSearchResult.class, accountEntity.accountId, user.username.username))
                .from(accountEntity)
                .where(filtersBuilded.and(accountEntity.userId.userUuid.eq(user.uuid.userUuid)))
                .fetch();
    }

    @Override
    public List<AccountDto> searchForXXXPage(Criteria filters) {
        var filtersBuilded = buildFilters(filters);

        var accounts = queryFactory
                .selectFrom(accountEntity)
                .where(filtersBuilded.and(accountEntity.userId.userUuid.eq(user.uuid.userUuid)))
                .fetch();

        return accounts.stream().map(AccountDto::new).toList();
    }

    @Override
    public Optional<AccountDto> findByUserPassword(final String password) {
        var userFound = queryFactory.selectFrom(user)
                .where(user.password.password.eq(password))
                .fetchOne();

        assert userFound != null;
        var accountFound = queryFactory.selectFrom(accountEntity)
                .where(accountEntity.userId.eq(userFound.uuid()))
                .fetchOne();

        return ofNullable(accountFound).map(AccountDto::new);
    }

    private BooleanBuilder buildFilters(final SearchAccount.Criteria filters) {
        BooleanBuilder builder = new BooleanBuilder();

        ofNullable(filters.username())
                .ifPresent(username -> builder.and(user.username.username.likeIgnoreCase("%" + username + "%")));

        ofNullable(filters.email())
                .ifPresent(email -> builder.and(user.email.email.eq(email)));

        ofNullable(filters.countryCode())
                .ifPresent(countryCode -> builder.and(user.country.eq(countryCode)));

        return builder;
    }
}


