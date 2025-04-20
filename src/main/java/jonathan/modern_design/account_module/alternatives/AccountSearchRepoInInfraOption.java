package jonathan.modern_design.account_module.alternatives;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jonathan.modern_design._common.annotations.DataAdapter;
import jonathan.modern_design.account_module.api.dtos.AccountDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

public interface AccountSearchRepoInInfraOption {
    Optional<AccountDto> findByUserPassword(final String password);

    List<AccountDto> search(Criteria filters);

    @Builder
    record Criteria(
            String username,
            String email,
            String countryCode
    ) {
    }
}

@DataAdapter
@RequiredArgsConstructor
//Has to have Impl in the name to avoid Spring mapping to JPARepository
class AccountSearchRepoImpl implements AccountSearchRepoInInfraOption {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<AccountDto> search(Criteria filters) {
        return null;
    }

    @Override
    public Optional<AccountDto> findByUserPassword(final String password) {
        return Optional.empty();
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
