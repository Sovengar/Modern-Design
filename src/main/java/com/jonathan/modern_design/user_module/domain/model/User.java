package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.country.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Slf4j
@Entity
@Table(name = "users", schema = "md")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class User extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SQ")
    @SequenceGenerator(name = "USERS_SQ", sequenceName = "MD.USERS_SQ", allocationSize = 1, initialValue = 1)
    private UserId id;

    @Column
    private UUID uuid;

    @Column
    private UserRealName realname;

    @Column
    private UserName username;

    @Column
    private UserEmail email;

    @Column
    private UserPassword password;

    @Column
    private Country country;

    public static User create(UUID uuid, String realname, String username, String email, String password, Country country) {
        return new User(new UserId(null), uuid, UserRealName.of(realname), UserName.of(username), UserEmail.of(email), UserPassword.of(password), country);
    }

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }

    public record UserId(Long value) {
    }
}
