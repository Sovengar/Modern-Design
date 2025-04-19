package jonathan.modern_design.account_module.domain;

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
import jonathan.modern_design._common.BaseEntity;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountId;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.user.domain.User.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

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

    public AccountEntity(Account account) {
        this.accountId = nonNull(account.accountId()) ? account.accountId().id() : null;
        this.accountNumber = account.accountAccountNumber().accountNumber();
        this.balance = account.money().amount();
        this.currency = account.money().currency();
        this.address = account.address().toString();
        this.userId = account.userId();
        this.dateOfLastTransaction = account.dateOfLastTransaction();
        this.active = account.active();
    }

    public Account toDomain() {
        return new Account(new AccountId(accountId), AccountAccountNumber.of(accountNumber), AccountMoney.of(balance, currency), AccountAddress.of(address), userId, dateOfLastTransaction, active);
    }

    @PrePersist
    public void prePersist() {
        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
        log.info("postPersist");
    }
}
