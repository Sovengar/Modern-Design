package jonathan.modern_design.auth.domain.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jonathan.modern_design._shared.infra.db.AuditingColumns;
import jonathan.modern_design._shared.tags.AggregateRoot;
import jonathan.modern_design._shared.tags.MicroType;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.vo.UserEmail;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.modulith.NamedInterface;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "users", schema = "auth")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
@AggregateRoot
public class User extends AuditingColumns {
    @EmbeddedId
    private Id id;
    @Embedded
    private UserName username;
    @Embedded
    private UserEmail email;
    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "internal_enterprise_email"))
    private UserEmail internalEnterpriseEmail;
    @Embedded
    @Getter(PRIVATE)
    private UserPassword password;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "role_code")
    private Role role; //Should be Role.Code, but since they are on the same BC and not a big graph, we can do the exception for pragmatism.
    @Version
    private Integer version;
    @Column(nullable = false)
    private boolean deleted = false;

    public Optional<String> getInternalEnterpriseEmail() {
        return internalEnterpriseEmail != null ? ofNullable(internalEnterpriseEmail.getEmail()) : Optional.empty();
    }

    public void delete() {
        //Comply with the law GDPR (General Data Protection Regulation)
        this.deleted = true;
        this.status = null;
        this.username = null;
        this.email = null;
        this.internalEnterpriseEmail = null;
        this.password = null;
    }

    public void changeRole(Role newRole) {
        requireNonNull(newRole);

        if (this.deleted) {
            throw new IllegalStateException("Cannot change role of a deleted auth.");
        }

        //An inactive auth can have their role changed to another role

        if (this.role.getCode().equals(newRole.getCode())) {
            throw new IllegalStateException("Cannot change role to the same role.");
        }

        if (Roles.ADMIN.getCode().equals(newRole.getCode().getRoleCode())) {
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
    @NamedInterface //Exposed to other modules
    public static class Id implements Serializable, MicroType {
        @Serial private static final long serialVersionUID = -2753108705494085826L;
        UUID userId;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static User register(Id id, UserName username, UserEmail email, UserPassword password, Role role) {
            var userId = id != null ? id : Id.of(UUID.randomUUID());
            var version = 0;
            var deleted = false;

            return new User(userId, requireNonNull(username), requireNonNull(email), null, requireNonNull(password), Status.DRAFT, requireNonNull(role), version, deleted);
        }

        public static User registerAdmin(Id id, UserName username, UserEmail email, UserEmail internalEmail, UserPassword password) {
            var userId = id != null ? id : Id.of(UUID.randomUUID());
            var version = 0;
            var deleted = false;

            return new User(userId, requireNonNull(username), requireNonNull(email), internalEmail, requireNonNull(password), Status.ACTIVE, Role.of(Roles.ADMIN), version, deleted);
        }
    }
}
