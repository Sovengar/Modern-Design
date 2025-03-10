package com.jonathan.modern_design.user_module.user.domain.model;

import com.jonathan.modern_design._infra.config.annotations.OptionalField;
import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.country.Country;
import com.jonathan.modern_design.user_module.role.Role;
import com.jonathan.modern_design.user_module.role.Roles;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Entity
@Table(name = "users", schema = "md")
@Getter
@NoArgsConstructor(access = PRIVATE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class User extends BaseEntity {
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
    private UserName username;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private UserEmail email;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "internal_enterprise_email"))
    @OptionalField
    private UserEmail internalEnterpriseEmail;
    @Embedded
    private UserPassword password;
    private String country;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;

    @Transient //TODO pasar a tabla
    private List<String> phoneNumbers = new ArrayList<>();

    @ManyToOne
    private Role role;

    public static User register(UserId uuid, UserRealName realname, UserName username, UserEmail email, UserPassword password, Country country, Role role) {
        return new User(null, uuid, realname, username, email, null, password, country.code(), Status.DRAFT, new ArrayList<>(), role);
    }

    public static User registerAdmin(UserId uuid, UserRealName realname, UserName username, UserEmail email, UserEmail internalEmail, UserPassword password, Country country) {
        return new User(null, uuid, realname, username, email, internalEmail, password, country.code(), Status.ACTIVE, new ArrayList<>(), Role.of(Roles.ADMIN));
    }

    public String getRealNameOrPlaceHolder() {
        return realname.getRealname().orElse("Anonymous");
    }

    public List<String> getPhoneNumbers() {
        return Collections.unmodifiableList(phoneNumbers);
    }

    public Optional<String> getInternalEnterpriseEmail() {
        return internalEnterpriseEmail != null ? ofNullable(internalEnterpriseEmail.getValue()) : Optional.empty();
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

    @Data //Not a record because ORM needs mutability
    @Setter(PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED) //For Hibernate
    @Embeddable
    public static class UserId implements Serializable {
        private UUID userUuid;
    }
}
