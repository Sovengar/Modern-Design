package jonathan.modern_design.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.api.Response;
import jonathan.modern_design._shared.tags.DataAdapter;
import jonathan.modern_design._shared.tags.WebAdapter;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.banking.queries.SearchAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static jonathan.modern_design.banking.domain.models.QAccountEntity.accountEntity;

@Slf4j
@RequiredArgsConstructor
@WebAdapter("/v1/search")
class IdkHttpController {
    private final IdkSearch querier;

    @Operation(description = "Search Account")
    @PostMapping("/search")
    public ResponseEntity<Response<List<AccountDto>>> searchForXXXPage(@RequestBody SearchAccount.Criteria filters) {
        var accountDtos = querier.searchForXXXPage(filters);
        return ResponseEntity.ok(new Response.Builder<List<AccountDto>>().data(accountDtos).withDefaultMetadataV1());
    }

    @Operation(description = "Search Account")
    @GetMapping(path = "/search/byuser/password/{password}")
    public ResponseEntity<Response<AccountDto>> findAccount(@PathVariable String password) {
        var accountDto = querier.searchWithUserPassword(password).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new Response.Builder<AccountDto>().data(accountDto).withDefaultMetadataV1());
    }
}


@DataAdapter
class IdkSearch {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public IdkSearch(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    public List<AccountDto> searchForXXXPage(SearchAccount.Criteria filters) {
        var filtersBuilded = buildFilters(filters);

        var accounts = queryFactory
                .selectFrom(accountEntity)
                //TODO .where(filtersBuilded.and(accountEntity.userId.userId.eq(user.id.userId)))
                .fetch();

        return accounts.stream().map(AccountDto::new).toList();
    }

    public Optional<AccountDto> searchWithUserPassword(final String password) {
        return empty();
        //TODO
//        var userFound = queryFactory.selectFrom(user)
//                .where(user.password.password.eq(password))
//                .fetchOne();
//
//        assert userFound != null;
//        var accountFound = queryFactory.selectFrom(accountEntity)
//                .where(accountEntity.userId.eq(userFound.getId()))
//                .fetchOne();
//
//        return ofNullable(accountFound).map(AccountDto::new);
    }

    private BooleanBuilder buildFilters(final SearchAccount.Criteria filters) {
        BooleanBuilder builder = new BooleanBuilder();

        //TODO
//        ofNullable(filters.username())
//                .ifPresent(username -> builder.and(user.username.username.likeIgnoreCase("%" + username + "%")));
//
//        ofNullable(filters.email())
//                .ifPresent(email -> builder.and(user.email.email.eq(email)));

        return builder;
    }
}
