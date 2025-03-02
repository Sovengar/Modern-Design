package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.annotations.Optional;
import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.country.Country;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "users", schema = "md")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class User extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SQ")
    @SequenceGenerator(name = "USERS_SQ", sequenceName = "MD.USERS_SQ", allocationSize = 1, initialValue = 1)
    @EmbeddedId
    private UserId id;
    private UUID uuid;
    @Optional
    private UserRealName realname;
    private UserName username;
    private UserEmail email;
    private UserPassword password;
    private String country;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Status status;
    private List<String> phoneNumbers = new ArrayList<>();

    @ManyToOne
    private Role role;

    public static User register(UUID uuid, String realname, String username, String email, String password, Country country) {
        return new User(new UserId(null), uuid, UserRealName.of(realname), UserName.of(username), UserEmail.of(email), UserPassword.of(password), country.code(), Status.DRAFT, new ArrayList<>(), null);
    }

    public static User registerAdmin(UUID uuid, String realname, String username, String email, String password, Country country) {
        return new User(new UserId(null), uuid, UserRealName.of(realname), UserName.of(username), UserEmail.of(email), UserPassword.of(password), country.code(), Status.ACTIVE, new ArrayList<>(), null); //TODO Roles.ADMIN
    }

    public String getRealNameOrPlaceHolder() {
        return realname.getValue().isEmpty() ? "Anonymous" : realname.getValue().get();
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

    @Embeddable
    public record UserId(Long value) {
    }
}
