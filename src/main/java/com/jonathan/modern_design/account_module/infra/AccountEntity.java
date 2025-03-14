package com.jonathan.modern_design.account_module.infra;

import com.jonathan.modern_design._infra.config.database.BaseEntity;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Slf4j
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class AccountEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "MD.ACCOUNTS_SQ", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long accountId; //Cant use microType with sequence
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private String address;
    private LocalDateTime dateOfLastTransaction;
    private boolean active;
    @Embedded
    private UserId userId;

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }
}
