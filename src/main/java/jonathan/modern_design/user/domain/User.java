package jonathan.modern_design.user.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design._common.annotations.OptionalField;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.user.domain.catalogs.Roles;
import jonathan.modern_design.user.domain.vo.UserEmail;
import jonathan.modern_design.user.domain.vo.UserPassword;
import jonathan.modern_design.user.domain.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.vo.UserRealName;
import jonathan.modern_design.user.domain.vo.UserUserName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Entity
@Table(name = "users", schema = "md")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
@Builder //For mapping and testing only!!!!!
@AggregateRoot
public class User extends AuditingColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SQ")
    @SequenceGenerator(name = "USERS_SQ", sequenceName = "MD.USERS_SQ", allocationSize = 1)
    private Long userId; //Cant use microType with sequence
    @Embedded
    private UserId uuid;
    @OptionalField
    @Embedded
    private UserRealName realname;
    @Embedded
    private UserUserName username;
    @Embedded
    private UserEmail email;
    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "internal_enterprise_email"))
    @OptionalField
    private UserEmail internalEnterpriseEmail;
    @Embedded
    private UserPassword password;
    private String country;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;

    @Embedded
    private UserPhoneNumbers userPhoneNumbers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    public static User register(UserId uuid, UserRealName realname, UserUserName username, UserEmail email, UserPassword password, Country country, UserPhoneNumbers phoneNumbers, Role role) {
        requireNonNull(country);

        return new User(
                null,
                uuid,
                realname,
                requireNonNull(username),
                requireNonNull(email),
                null,
                requireNonNull(password),
                country.code(),
                Status.DRAFT,
                phoneNumbers,
                requireNonNull(role));
    }

    public static User registerAdmin(UserId uuid, UserRealName realname, UserUserName username, UserEmail email, UserEmail internalEmail, UserPassword password, UserPhoneNumbers phoneNumbers, Country country) {
        requireNonNull(country);

        return new User(
                null,
                uuid,
                realname,
                requireNonNull(username),
                requireNonNull(email),
                internalEmail,
                requireNonNull(password),
                country.code(),
                Status.ACTIVE,
                phoneNumbers,
                Role.of(Roles.ADMIN));
    }

    public String getRealNameOrPlaceHolder() {
        return realname.getRealname().orElse("Anonymous");
    }

    public Optional<String> getInternalEnterpriseEmail() {
        return internalEnterpriseEmail != null ? ofNullable(internalEnterpriseEmail.email()) : Optional.empty();
    }

    public void changeRole(Role newRole) {
        requireNonNull(newRole);

        if (this.status == Status.DELETED) {
            throw new IllegalStateException("Cannot change role of a deleted user.");
        }

        if (this.role.code().equals(newRole.code())) {
            throw new IllegalStateException("Cannot change role to the same role.");
        }

        if (newRole.code().roleCode().equals(Roles.ADMIN.code())) {
            throw new IllegalStateException("Cannot change the role of an ADMIN.");
        }

        this.role = newRole;
    }

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }

    public enum Status {
        DRAFT, ACTIVE, DELETED
    }

    //TODO MIGRATE TO @VALUE?
    @Data //Not a record because ORM needs mutability
    @Setter(PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED) //For Hibernate
    @Embeddable
    public static class UserId implements Serializable {
        @Serial private static final long serialVersionUID = -2753108705494085826L;
        private UUID userUuid;
    }
}
