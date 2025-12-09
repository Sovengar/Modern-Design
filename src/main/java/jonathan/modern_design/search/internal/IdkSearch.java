package jonathan.modern_design.search.internal;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.banking.api.AccountQueryApi;
import jonathan.modern_design.banking.api.dtos.AccountDto;
import jonathan.modern_design.search.store.AccountWithUserInfoRepo;
import jonathan.modern_design.search.view_models.AccountWithUserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

@DataAdapter
class IdkSearch {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final AccountQueryApi accountQueryApi;
    private final AuthApi authApi;
    private final AccountWithUserInfoRepo accountWithUserInfoRepo;

    public IdkSearch(EntityManager entityManager, AccountQueryApi accountQueryApi, AuthApi authApi, AccountWithUserInfoRepo accountWithUserInfoRepo) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
        this.accountQueryApi = accountQueryApi;
        this.authApi = authApi;
        this.accountWithUserInfoRepo = accountWithUserInfoRepo;
    }

    public Optional<AccountWithUserInfo> findAccountWithUserInfo(UUID userId) {
        var user = authApi.findUser(User.Id.of(userId));
        var account = accountQueryApi.findByUserId(userId);

        //Searching doing query composition into a ViewModel
        var result1 = Optional.of(new AccountWithUserInfo(account.accountNumber(), account.balance(), user.username(), user.email()));

        //Searching our readModel to avoid querying other modules
        var result2 = accountWithUserInfoRepo.findById(userId).map(acc -> new AccountWithUserInfo(acc.getAccountNumber(), acc.getBalance(), acc.getUsername(), acc.getEmail()));

        return result1.or(() -> result2);
    }

    public List<AccountDto> searchForXXXPage(AccountCriteria filters) {
        return List.of();
        //var filtersBuilded = buildFilters(filters);
        //
        //var accounts = queryFactory
        //        .selectFrom(accountEntity)
        //        .where(filtersBuilded.and(accountEntity.userId.userId.eq(user.id.userId)))
        //        .fetch();
        //
        //return accounts.stream().map(AccountDto::new).toList();
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

    private BooleanBuilder buildFilters(final AccountCriteria filters) {
        BooleanBuilder builder = new BooleanBuilder();

        //TODO
//        ofNullable(filters.username())
//                .ifPresent(username -> builder.and(user.username.username.likeIgnoreCase("%" + username + "%")));
//
//        ofNullable(filters.email())
//                .ifPresent(email -> builder.and(user.email.email.eq(email)));

        return builder;
    }

    public record AccountCriteria(String username, String email) {
    }
}
