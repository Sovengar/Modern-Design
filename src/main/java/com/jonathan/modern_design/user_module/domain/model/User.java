package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.annotations.Optional;
import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.country.Country;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private UUID uuid;
    @Optional
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "realname"))
    private UserRealName realname;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private UserName username;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private UserEmail email;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private UserPassword password;
    private String country;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;

    @Transient //TODO pasar a tabla
    private List<String> phoneNumbers = new ArrayList<>();

    @ManyToOne
    private Role role;

    public static User register(UUID uuid, String realname, String username, String email, String password, Country country) {
        return new User(null, uuid, UserRealName.of(realname), UserName.of(username), UserEmail.of(email), UserPassword.of(password), country.code(), Status.DRAFT, new ArrayList<>(), null);
    }

    public static User registerAdmin(UUID uuid, String realname, String username, String email, String password, Country country) {
        return new User(null, uuid, UserRealName.of(realname), UserName.of(username), UserEmail.of(email), UserPassword.of(password), country.code(), Status.ACTIVE, new ArrayList<>(), null); //TODO Roles.ADMIN
    }

    public String getRealNameOrPlaceHolder() {
        return realname.getValue().orElse("Anonymous");
    }

    public List<String> getPhoneNumbers() {
        return Collections.unmodifiableList(phoneNumbers);
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
}
