package com.jonathan.modern_design.user_module.domain.model;

import com.jonathan.modern_design._infra.config.database.BaseEntity;
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
import lombok.Builder;
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
@Builder //TODO QUITAR
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
    private String country;

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
