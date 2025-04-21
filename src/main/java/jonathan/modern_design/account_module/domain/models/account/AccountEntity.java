package jonathan.modern_design.account_module.domain.models.account;

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
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountId;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.user.domain.models.User.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "accounts", schema = "md")
@Getter
@Setter //For updates, allowed because is a data model only
@Builder //For mapping and testing
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class AccountEntity extends AuditingColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "MD.ACCOUNTS_SQ", allocationSize = 1)
    private Long accountId; //Cant use microType with sequence
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private String address;
    private boolean active;
    @Embedded
    private UserId userId;

    public static AccountEntity create(Account account) {
        //If we start to use uuid from the client, we could assign the id directly
        var accountId = nonNull(account.accountId()) ? account.accountId().id() : null;

        return new AccountEntity(
                accountId,
                account.accountAccountNumber().accountNumber(),
                account.money().balance(),
                account.money().currency(),
                account.address().toString(),
                account.active(),
                account.userId()
        );
    }

    public Account toDomain() {
        return new Account(new AccountId(accountId), AccountAccountNumber.of(accountNumber), AccountMoney.of(balance, currency), AccountAddress.of(address), userId, active);
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
