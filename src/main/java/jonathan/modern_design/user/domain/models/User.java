package jonathan.modern_design.user.domain.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design._common.annotations.ValueObject;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.user.domain.catalogs.Roles;
import jonathan.modern_design.user.domain.models.vo.UserEmail;
import jonathan.modern_design.user.domain.models.vo.UserPassword;
import jonathan.modern_design.user.domain.models.vo.UserPhoneNumbers;
import jonathan.modern_design.user.domain.models.vo.UserRealName;
import jonathan.modern_design.user.domain.models.vo.UserUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "users", schema = "md")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
@AggregateRoot
public class User extends AuditingColumns {
    @EmbeddedId
    private Id id;
    @Embedded
    @Getter(PRIVATE)
    private UserRealName realname;
    @Embedded
    private UserUserName username;
    @Embedded
    private UserEmail email;
    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "internal_enterprise_email"))
    private UserEmail internalEnterpriseEmail;
    @Embedded
    private UserPassword password;
    private String country;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;
    @Embedded
    private UserPhoneNumbers userPhoneNumbers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code")
    private Role role; //We should not reference another AR directly, doing the exception here
    @Version
    private Integer version;
    @Column(nullable = false)
    private boolean deleted = false;

    public String getRealNameOrPlaceHolder() {
        return realname.getRealname().orElse("Not defined");
    }

    public Optional<String> getInternalEnterpriseEmail() {
        return internalEnterpriseEmail != null ? ofNullable(internalEnterpriseEmail.getEmail()) : Optional.empty();
    }

    public void delete() {
        //Comply with the law GDPR (General Data Protection Regulation)
        this.deleted = true;
        this.status = null;
        this.realname = null;
        this.username = null;
        this.email = null;
        this.internalEnterpriseEmail = null;
        this.password = null;
        this.country = "";
        this.userPhoneNumbers = null;
    }

    public void changeRole(Role newRole) {
        requireNonNull(newRole);

        if (this.deleted) {
            throw new IllegalStateException("Cannot change role of a deleted user.");
        }

        //An inactive user can have their role changed to another role

        if (this.role.getCode().equals(newRole.getCode())) {
            throw new IllegalStateException("Cannot change role to the same role.");
        }

        if (newRole.getCode().getRoleCode().equals(Roles.ADMIN.getCode())) {
            throw new IllegalStateException("Cannot change the role of an ADMIN.");
        }

        this.role = newRole;
    }

    @PrePersist
    public void prePersist() {
    }

    @PostPersist
    public void postPersist() {
    }

    public enum Status {
        DRAFT, ACTIVE, INACTIVE
    }

    @Embeddable
    @Value //Not a record for Hibernate
    @NoArgsConstructor(access = PACKAGE, force = true) //For Hibernate
    @RequiredArgsConstructor(staticName = "of")
    public static class Id implements Serializable, ValueObject {
        @Serial private static final long serialVersionUID = -2753108705494085826L;
        UUID userId;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static User register(Id id, UserRealName realname, UserUserName username, UserEmail email, UserPassword password, Country country, UserPhoneNumbers phoneNumbers, Role role) {
            var userId = id != null ? id : Id.of(UUID.randomUUID());
            var version = 0;
            var deleted = false;

            return new User(userId, realname, requireNonNull(username), requireNonNull(email), null, requireNonNull(password), requireNonNull(country).code(), Status.DRAFT, phoneNumbers, requireNonNull(role), version, deleted);
        }

        public static User registerAdmin(Id id, UserRealName realname, UserUserName username, UserEmail email, UserEmail internalEmail, UserPassword password, UserPhoneNumbers phoneNumbers, Country country) {
            var userId = id != null ? id : Id.of(UUID.randomUUID());
            var version = 0;
            var deleted = false;

            return new User(userId, realname, requireNonNull(username), requireNonNull(email), internalEmail, requireNonNull(password), requireNonNull(country).code(), Status.ACTIVE, phoneNumbers, Role.of(Roles.ADMIN), version, deleted);
        }
    }
}
