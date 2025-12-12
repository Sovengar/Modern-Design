package jonathan.modern_design.auth.domain.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.api.events.auth.UserSnapshot;
import jonathan.modern_design._shared.domain.vo.Email;
import jonathan.modern_design._shared.infra.BaseAggregateRoot;
import jonathan.modern_design._shared.tags.models.AggregateRoot;
import jonathan.modern_design._shared.tags.persistence.InMemoryOnlyCatalog;
import jonathan.modern_design._shared.tags.persistence.MicroType;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;
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

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "users", schema = "auth")
@Getter
@NoArgsConstructor(access = PRIVATE) //For Hibernate
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
@AggregateRoot
public class User extends BaseAggregateRoot<User> {
    @EmbeddedId
    private Id id;
    @Embedded
    private UserName username;
    @Embedded
    private Email email;
    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "internal_enterprise_email"))
    private Email internalEnterpriseEmail;
    @Embedded
    @Getter(PRIVATE)
    private UserPassword password;

    @InMemoryOnlyCatalog
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_code")
    private Role role; //Should be Role.Code, but since they are on the same BC and not a big graph, we can do the exception for pragmatism.
    @Column(nullable = false)
    private boolean deleted = false;

    private User(Id id, UserName username, Email email, Email internalEmail, UserPassword password, UserStatus userStatus, Role role) {
        this.id = nonNull(id) ? id : Id.of(UUID.randomUUID());
        this.username = requireNonNull(username);
        this.email = requireNonNull(email);
        this.internalEnterpriseEmail = internalEmail;
        this.password = requireNonNull(password);
        this.status = requireNonNull(userStatus);
        this.role = requireNonNull(role);
        this.deleted = false;

        this.registerEvent(new UserSnapshot(this.id.getUserId(), this.username.getUsername(), this.email.getEmail()));
    }

    public Optional<String> getInternalEnterpriseEmail() {
        if (Roles.ADMIN.getCode().equals(this.role.getCode().getRoleCode())) {
            return Optional.of(internalEnterpriseEmail.getEmail());
        } else {
            return empty();
        }
    }

    public void delete() {
        //Comply with the law GDPR (General Data Protection Regulation)
        this.deleted = true;
        this.status = null;
        this.username = null;
        this.email = null;
        this.internalEnterpriseEmail = null;
        this.password = null;

        this.registerEvent(new UserSnapshot(id.getUserId(), null, null));
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

        if (Roles.ADMIN.getCode().equals(newRole.getCode().getRoleCode())) {
            throw new IllegalStateException("Cannot change the role of an ADMIN.");
        }

        this.role = newRole;
    }

    @Getter
    public enum UserStatus {
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
        public static User register(Id id, UserName username, Email email, UserPassword password, Role role) {
            return new User(id, requireNonNull(username), requireNonNull(email), null, requireNonNull(password), UserStatus.DRAFT, requireNonNull(role));
        }

        public static User registerAdmin(Id id, UserName username, Email email, Email internalEmail, UserPassword password) {
            return new User(id, requireNonNull(username), requireNonNull(email), internalEmail, requireNonNull(password), UserStatus.ACTIVE, Role.of(Roles.ADMIN));
        }
    }
}
