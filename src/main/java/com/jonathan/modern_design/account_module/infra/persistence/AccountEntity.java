package com.jonathan.modern_design.account_module.infra.persistence;

import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts", schema = "md")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class AccountEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "MD.ACCOUNTS_SQ", allocationSize = 1, initialValue = 1)
    @EmbeddedId
    private Long id;
    @Column(name = "account_number")
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Currency currency;
    private String address;
    private LocalDateTime dateOfLastTransaction;
    private boolean active;
    private Long userId;

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }

    @Embeddable
    public record AccountId(Long value) {
    }
}
