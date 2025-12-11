package jonathan.modern_design.banking.infra.store;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jonathan.modern_design.banking.domain.models.AccountEntity;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountSpecifications {
    /**
     * Crea una especificación para filtrar por dirección del titular de la cuenta
     *
     * @param address La dirección a buscar
     * @return Una especificación para el filtrado por dirección
     */
    public static Specification<AccountEntity> hasAddress(AccountHolderAddress address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null) {
                return null;
            }

            Join<AccountEntity, AccountHolder> holderJoin = root.join("accountHolder", JoinType.INNER);

            // Construye condiciones para cada campo no nulo de la dirección
            return criteriaBuilder.and(
                    StringUtils.hasLength(address.getStreet())
                            ? criteriaBuilder.equal(holderJoin.get("address").get("street"), address.getStreet())
                            : null,
                    StringUtils.hasLength(address.getCity())
                            ? criteriaBuilder.equal(holderJoin.get("address").get("city"), address.getCity())
                            : null,
                    StringUtils.hasLength(address.getState())
                            ? criteriaBuilder.equal(holderJoin.get("address").get("state"), address.getState())
                            : null,
                    StringUtils.hasLength(address.getPostalCode())
                            ? criteriaBuilder.equal(holderJoin.get("address").get("postalCode"), address.getPostalCode())
                            : null
            );
        };
    }

    /**
     * Crea una especificación para filtrar por ciudad del titular
     *
     * @param city La ciudad a buscar
     * @return Una especificación para el filtrado por ciudad
     */
    public static Specification<AccountEntity> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasLength(city)) {
                return null;
            }

            Join<AccountEntity, AccountHolder> holderJoin = root.join("accountHolder", JoinType.INNER);
            return criteriaBuilder.equal(holderJoin.get("address").get("city"), city);
        };
    }

    /**
     * Crea una especificación para filtrar por balance de cuenta
     *
     * @param minBalance El balance mínimo
     * @return Una especificación para el filtrado por balance mínimo
     */
    public static Specification<AccountEntity> hasBalanceGreaterThan(BigDecimal minBalance) {
        return (root, query, criteriaBuilder) -> {
            if (minBalance == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("balance"), minBalance);
        };
    }

    /**
     * Crea una especificación para filtrar cuentas por nombre del titular
     *
     * @param fullName El nombre del titular a buscar (búsqueda parcial)
     * @return Una especificación para el filtrado por nombre
     */
    public static Specification<AccountEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasLength(fullName)) {
                return null; // Si el valor es nulo o vacío, no se aplica este filtro
            }

            Join<AccountEntity, AccountHolder> holderJoin = root.join("accountHolder", JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.upper(holderJoin.get("name").get("name")),
                    "%" + fullName.toUpperCase() + "%"
            );
        };
    }

    /**
     * Crea una especificación para filtrar cuentas por fecha de nacimiento del titular
     *
     * @param birthdate La fecha de nacimiento a buscar
     * @return Una especificación para el filtrado por fecha de nacimiento
     */
    public static Specification<AccountEntity> hasBirthdate(LocalDate birthdate) {
        return (root, query, criteriaBuilder) -> {
            if (birthdate == null) {
                return null; // Si el valor es nulo, no se aplica este filtro
            }

            Join<AccountEntity, AccountHolder> holderJoin = root.join("accountHolder", JoinType.INNER);
            return criteriaBuilder.equal(
                    holderJoin.get("birthdate").get("birthdate"),
                    birthdate
            );
        };
    }

    /**
     * Crea una especificación para ordenar los resultados por fecha de creación
     *
     * @param spec La especificación base a la que se añadirá el ordenamiento
     * @return Una especificación con ordenamiento por fecha de creación
     */
    public static Specification<AccountEntity> orderByCreatedDate(Specification<AccountEntity> spec) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return spec == null ? null : spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
