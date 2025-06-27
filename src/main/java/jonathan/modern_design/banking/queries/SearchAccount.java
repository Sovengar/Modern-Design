package jonathan.modern_design.banking.queries;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.infra.store.read_model.AccountTransactionsViewRepository;
import jonathan.modern_design.banking.infra.store.spring.AccountRepoSpringDataJPA;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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
import static jonathan.modern_design._shared.infra.TraceIdGenerator.generateTraceId;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;
import static jonathan.modern_design.banking.domain.models.QAccountHolder.accountHolder;

public interface SearchAccount {
    List<AccountIdWithHolderFullName> searchWithJPQL(Criteria filters);

    List<AccountIdWithHolderFullName> searchWithQueryDSL(Criteria filters);

    List<AccountDto> searchWithAddress(AccountHolderAddress address);

    Page<AccountDto> searchWithPagination(final Pageable pageable, final Criteria filters);

    List<AccountDto> searchWithCity(final String city);

    @Builder
    record Criteria(
            String fullName,
            LocalDate birthdate
    ) {
    }

    record AccountIdWithHolderFullName(Long accountId, String fullName) {
    }
}

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/accounts")
class SearchAccountHttpController {
    private final SearchAccountQueryImpl querier;

    @Operation(description = "Search Account")
    @PostMapping("/search/xxxPage")
    public ResponseEntity<Response<List<SearchAccount.AccountIdWithHolderFullName>>> searchProjectionForXXXPage(@RequestBody SearchAccount.Criteria filters) {
        var accountSearchResults = querier.searchWithQueryDSL(filters);
        return ResponseEntity.ok(new Response.Builder<List<SearchAccount.AccountIdWithHolderFullName>>().data(accountSearchResults).withDefaultMetadataV1());
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
//Has to have Impl in the name to avoid Spring mapping to JPARepository
class SearchAccountQueryImpl implements SearchAccount {
    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountRepoSpringDataJPA repository;
    private final JPAQueryFactory queryFactory;

    public SearchAccountQueryImpl(EntityManager entityManager, AccountRepoSpringDataJPA repository, AccountTransactionsViewRepository viewRepository) {
        this.repository = repository;
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    @Override
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

    @Override
    public List<AccountIdWithHolderFullName> searchWithQueryDSL(Criteria filters) {
        var filtersBuilded = buildFilters(filters);

        return queryFactory
                .select(Projections.constructor(AccountIdWithHolderFullName.class, accountEntity.id, accountHolder.name.name))
                .from(accountEntity)
                .join(accountEntity.accountHolder, accountHolder)
                .where(filtersBuilded)
                .fetch();
    }

    @Override
    public Page<AccountDto> searchWithPagination(final Pageable pageable, final Criteria filters) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(Account::new)
                .toList();

        //This is bad, there is no filter. Just showing findAll pageable from Spring Data JPA
        var accountsDto = accounts.stream().map(AccountDto::new).toList();
        return new PageImpl<>(accountsDto, pageable, accounts.size());
    }

    @Override
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

    @Override
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

    private BooleanBuilder buildFilters(final SearchAccount.Criteria filters) {
        BooleanBuilder builder = new BooleanBuilder();

        ofNullable(filters.fullName())
                .ifPresent(fullName -> builder.and(accountHolder.name.name.likeIgnoreCase("%" + fullName + "%")));

        ofNullable(filters.birthdate())
                .ifPresent(birthdate -> builder.and(accountHolder.birthdate.birthdate.eq(birthdate)));

        return builder;
    }
}


