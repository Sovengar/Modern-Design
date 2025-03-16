package jonathan.modern_design.account_module.infra;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.Repo;
import jonathan.modern_design.account_module.dtos.AccountDto;
import jonathan.modern_design.user.domain.QUser;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;

public interface AccountSearchRepo {
    Optional<AccountDto> findByUserPassword(final String password);

    List<AccountDto> search(AccountSearchCriteria filters);

    @Builder
    record AccountSearchCriteria(
            String username,
            String email,
            String countryCode
    ) {
    }
}

@Repo
@RequiredArgsConstructor
//Has to have Impl in the name to avoid Spring mapping to JPARepository
class AccountSearchRepoImpl implements AccountSearchRepo {

    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountDto> search(AccountSearchCriteria filters) {
        // Alternative: Spring Specifications https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html
        String jpql = "SELECT new com.jonathan.modern_design.account_module.application.FindAccountUseCase.AccountSearchResult(c.id, c.name)" +
                " FROM Customer c " +
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
        var query = entityManager.createQuery(jpql + whereCriteria, AccountDto.class);
        for (var entry : params.entrySet()) {
            query.setParameter(entry.getKey(), params.get(entry.getKey()));
        }
        return query.getResultList();
    }

    @Override
    public Optional<AccountDto> findByUserPassword(final String password) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        QUser user = QUser.user;
        QAccountEntity account = QAccountEntity.accountEntity;

        var userFound = queryFactory.selectFrom(user)
                .where(user.password.password.eq(password))
                .fetchOne();

        assert userFound != null;
        var accountFound = queryFactory.selectFrom(account)
                .where(account.userId.eq(userFound.getUuid()))
                .fetchOne();
//TODO
        return Optional.ofNullable(accountFound).map(accountEntity -> new AccountDto("a", BigDecimal.ZERO, "c", null));
    }
}

//region CriteriaAPI alternative
// add to pom <dependency>
//            <!--Generate Criteria API metamodel in target/generated-sources/annotations-->
//            <groupId>org.hibernate</groupId>
//            <artifactId>hibernate-jpamodelgen</artifactId>
//        </dependency>
//   public List<CustomerSearchResult> searchWithCriteria(CustomerSearchCriteria criteria) {
//      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//      CriteriaQuery<CustomerSearchResult> criteriaQuery = cb.createQuery(CustomerSearchResult.class);
//      Root<Customer> root = criteriaQuery.from(Customer.class);
//      criteriaQuery.select(cb.construct(CustomerSearchResult.class, root.get(Customer_.id), root.get(Customer_.name)));
//      List<Predicate> predicates = new ArrayList<>();
//      predicates.add(cb.isTrue(cb.literal(true)));
//
//      if (criteria.getName() != null) {
//         predicates.add(cb.like(cb.upper(root.get(Customer_.name)), cb.upper(cb.literal("%" + criteria.getName() + "%"))));
//      }
//
//      if (criteria.getEmail() != null) {
//         predicates.add(cb.equal(cb.upper(root.get(Customer_.email)), cb.upper(cb.literal(criteria.getEmail()))));
//      }
//
//      if (criteria.getCountryId() != null) {
//         predicates.add(cb.equal(root.get(Customer_.country).get(Country_.id), criteria.getCountryId()));
//      }
//      criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
//
//      TypedQuery<CustomerSearchResult> query = entityManager.createQuery(criteriaQuery);
//      return query.getResultList();
//   }
//endregion
