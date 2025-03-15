package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Fake;
import com.jonathan.modern_design._infra.config.annotations.PersistenceAdapter;
import com.jonathan.modern_design.account_module.application.AccountSearcher;
import com.jonathan.modern_design.account_module.domain.AccountRepo;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountResource;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface AccountSpringRepo extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByAccountNumber(@NonNull String accountNumber);
}

@PersistenceAdapter
@Primary //When Spring finds AccountRepository but creates AccountSpringRepo, it will use AccountRepositorySpringAdapter
@RequiredArgsConstructor
class AccountRepoAdapter implements AccountRepo {
    private final AccountSpringRepo repository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> findOne(final String accountNumber) {
        return findOneEntity(accountNumber).map(accountMapper::toAccount);
    }

    private Optional<AccountEntity> findOneEntity(@NonNull final String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    @Override
    public Page<Account> findAll(final Pageable pageable) {
        List<Account> accounts = repository.findAll(pageable)
                .getContent()
                .stream()
                .map(accountMapper::toAccount)
                .toList();

        return new PageImpl<>(accounts, pageable, accounts.size());
    }

    @Override
    public AccountNumber create(final Account account) {
        var accountEntity = accountMapper.toAccountEntity(account);
        repository.save(accountEntity);
        return account.getAccountNumber();
    }

    @Override
    public void update(final Account account) {
        var accountEntity = findOneEntity(account.getAccountNumber().getValue()).orElseThrow();
        accountMapper.updateAccountEntity(account, accountEntity);
        repository.save(accountEntity);
    }

    @Override
    public void delete(final String accountNumber) {
        repository.deleteById(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        this.findOneEntity(accountNumber).ifPresent(account -> {
            account.setDeleted(true);
            repository.save(account);
        });
    }
}

@Fake //This class is for unit tests, also, don't evaluate his state, pointless, rather evaluate the state of the objects
class AccountInMemoryRepo implements AccountRepo {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findOne(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return Optional.ofNullable(account);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        List<Account> accountsList = new ArrayList<>(accounts.values());
        return new PageImpl<>(accountsList, pageable, accountsList.size());
    }

    @Override
    public AccountNumber create(Account account) {
        accounts.put(account.getAccountNumber().getValue(), account);
        return account.getAccountNumber();
    }

    @Override
    public void update(Account account) {
        requireNonNull(account);
    }

    @Override
    public void delete(final String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public void softDelete(final String accountNumber) {
        accounts.remove(accountNumber);
    }

}

@Repository
@RequiredArgsConstructor
class AccountSearchRepoAdapter implements AccountSearcher {
    private final EntityManager entityManager;

    @Override
    public List<AccountResource> search(AccountSearcher.AccountSearchCriteria filters) {
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
        var query = entityManager.createQuery(jpql + whereCriteria, AccountResource.class);
        for (var entry : params.entrySet()) {
            query.setParameter(entry.getKey(), params.get(entry.getKey()));
        }
        return query.getResultList();
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

}
