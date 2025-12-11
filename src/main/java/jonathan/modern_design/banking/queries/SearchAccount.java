package jonathan.modern_design.banking.queries;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.infra.store.repositories.spring_jpa.AccountSpringJpaRepo;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.ACCOUNTS_RESOURCE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.BankingUrls.BANKING_MODULE_URL;
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;
import static jonathan.modern_design.banking.domain.models.QAccountHolder.accountHolder;
import static jonathan.modern_design.banking.infra.store.AccountSpecifications.hasBirthdate;
import static jonathan.modern_design.banking.infra.store.AccountSpecifications.hasFullName;

@Slf4j
@RequiredArgsConstructor
@WebAdapter(BANKING_MODULE_URL + ACCOUNTS_RESOURCE_URL)
class SearchAccountHttpController {
    private final SearchAccount querier;

    @Operation(description = "Search Account")
    @PostMapping("/search/xxxPage")
    public ResponseEntity<Response<Page<SearchAccount.AccountIdWithHolderFullName>>> searchProjectionForXXXPage(@RequestBody SearchAccount.Criteria filters, Pageable pageable) {
        var accountSearchResults = querier.searchWithQueryDSL(filters, pageable);
        return ResponseEntity.ok(new Response.Builder<Page<SearchAccount.AccountIdWithHolderFullName>>().data(accountSearchResults).withDefaultMetadataV1());
    }

    @Operation(description = "Find Accounts")
    @GetMapping(path = "/search/address")
    ResponseEntity<Response<List<AccountDto>>> loadAccount(@RequestBody AccountHolderAddress address) {
        Assert.state(Objects.nonNull(address), "Address is required");
        generateTraceId();
        //Authentication + Authorization

        log.info("BEGIN FindAccount for address: {}", address);
        var accounts = querier.searchWithAddress(address);
        var accountsByCity = querier.searchWithCity(address.getCity());
        log.info("END FindAccount for address: {}", address);

        return ResponseEntity.ok(new Response.Builder<List<AccountDto>>().data(accounts).withDefaultMetadataV1());
    }
}

@DataAdapter
public class SearchAccount {
    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountSpringJpaRepo repository;
    private final JPAQueryFactory queryFactory;

    public SearchAccount(EntityManager entityManager, AccountSpringJpaRepo repository) {
        this.repository = repository;
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    public List<AccountIdWithHolderFullName> searchWithJPQL(Criteria filters) {
        // Alternative: Spring Specifications https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html
        String jpql = "SELECT new jonathan.modern_design.banking.queries.SearchAccount.AccountSearchResult(a.id, a.name)" +
                " FROM Account a " +
                " WHERE ";
        List<String> jpqlParts = new ArrayList<>();
        jpqlParts.add("1=1"); // alternatives: Criteria API ± Spring Specifications or Query DSL
        Map<String, Object> params = new HashMap<>();

        ofNullable(filters.fullName())
                .ifPresent(fullName -> {
                    jpqlParts.add("UPPER(c.fullName) LIKE UPPER('%' || :fullName || '%')");
                    params.put("fullName", fullName);
                });

        ofNullable(filters.birthdate())
                .ifPresent(birthdate -> {
                    jpqlParts.add("UPPER(c.birthdate) = UPPER(:birthdate)");
                    params.put("birthdate", birthdate);
                });

        String whereCriteria = join(" AND ", jpqlParts);
        var query = entityManager.createQuery(jpql + whereCriteria, AccountIdWithHolderFullName.class);
        for (var entry : params.entrySet()) {
            query.setParameter(entry.getKey(), params.get(entry.getKey()));
        }
        return query.getResultList();
    }

    public Page<AccountIdWithHolderFullName> searchWithQueryDSL(Criteria filters, Pageable pageable) {
        var filtersBuilded = buildFilters(filters);

        var query = queryFactory
                .select(Projections.constructor(AccountIdWithHolderFullName.class, accountEntity.id, accountHolder.name.name))
                .from(accountEntity)
                .join(accountEntity.accountHolder, accountHolder)
                .where(filtersBuilded);

        applyPaginationWithQueryDsl(pageable, query, AccountEntity.class);
        var result = query.fetch();

        return new PageImpl<>(result, pageable, query.fetchCount());
    }

    public Page<AccountDto> searchWithPagination(final Pageable pageable, final Criteria filters) {
        Specification<AccountEntity> spec = null;

        if (StringUtils.hasLength(filters.fullName())) {
            spec = spec == null ? hasFullName(filters.fullName()) : spec.and(hasFullName(filters.fullName()));
        }

        if (filters.birthdate() != null) {
            spec = spec == null ? hasBirthdate(filters.birthdate()) : spec.and(hasBirthdate(filters.birthdate()));
        }

        var page = spec == null ? repository.findAll(pageable) : repository.findAll(spec, pageable);
        return page.map(AccountDto::new);
    }

    public List<AccountDto> searchWithCity(final String city) {

        List<AccountEntity> accounts = entityManager
                .createNativeQuery("""
                        SELECT a.* FROM banking.accounts a
                        INNER JOIN banking.account_holders ah ON a.account_holder_id = ah.account_holder_id
                        WHERE ah.address->>'city' = :city
                        """, AccountEntity.class)
                .setParameter("city", city)
                .getResultList();

        return accounts.stream().map(AccountDto::new).toList();
    }

    public List<AccountDto> searchWithAddress(final AccountHolderAddress address) {

        List<AccountEntity> accounts = entityManager
                .createNativeQuery("""
                        SELECT a.* FROM banking.accounts a
                        INNER JOIN banking.account_holders ah ON a.account_holder_id = ah.account_holder_id
                        WHERE ah.address @>':address'
                        """, AccountEntity.class)
                .setParameter("address", address)
                .getResultList();

        return accounts.stream().map(AccountDto::new).toList();
    }

    private BooleanBuilder buildFilters(final Criteria filters) {
        BooleanBuilder builder = new BooleanBuilder();

        ofNullable(filters.fullName())
                .ifPresent(fullName -> builder.and(accountHolder.name.name.likeIgnoreCase("%" + fullName + "%")));

        ofNullable(filters.birthdate())
                .ifPresent(birthdate -> builder.and(accountHolder.birthdate.birthdate.eq(birthdate)));

        return builder;
    }

    private void applyPaginationWithQueryDsl(final Pageable pageable, final JPAQuery<?> query, Class clazz) {
        if (pageable.getOffset() > 1000) {
            throw new RuntimeException("Too many results");
        }
        Querydsl querydsl = new Querydsl(Objects.requireNonNull(entityManager), (new PathBuilderFactory()).create(clazz));
        querydsl.applyPagination(pageable, query);
    }

    @Builder
    public record Criteria(
            String fullName,
            LocalDate birthdate
    ) {
    }

    public record AccountIdWithHolderFullName(Long accountId, String fullName) {
    }
}


