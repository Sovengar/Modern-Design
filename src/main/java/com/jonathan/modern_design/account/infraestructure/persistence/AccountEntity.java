package com.jonathan.modern_design.account.infraestructure.persistence;

import com.jonathan.modern_design.common.BaseEntity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account", schema = "md")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
@Slf4j
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal amount;

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }
}
