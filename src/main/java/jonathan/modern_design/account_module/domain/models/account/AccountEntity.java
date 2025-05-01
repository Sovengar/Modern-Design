package jonathan.modern_design.account_module.domain.models.account;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.user.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "accounts", schema = "md")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@SQLRestriction("deleted <> true") //Make Hibernate ignore soft deleted entries
public class AccountEntity extends AuditingColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "MD.ACCOUNTS_SQ", allocationSize = 1)
    private Long accountId; //Can't use microType with a sequence
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private String address;
    private boolean active;
    @Embedded
    private User.Id userId;

    public Account toDomain() {
        return new Account(new Account.Id(accountId), AccountAccountNumber.of(accountNumber), AccountMoney.of(balance, currency), AccountAddress.of(address), userId, active);
    }

    public void updateFrom(Account account) {
        this.accountId = account.accountId().id();
        this.accountNumber = account.accountAccountNumber().accountNumber();
        this.balance = account.money().balance();
        this.currency = account.money().currency();
        this.address = account.address().toString();
        this.active = account.active();
        this.userId = account.userId();
    }

    @PrePersist
    public void prePersist() {
        active = true;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static AccountEntity create(Account account) {
            //If we start to use id from the client, we could assign the id directly
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

        public static AccountEntity create(Long accountId, String accountNumber, BigDecimal balance, Currency currency, String address, boolean isActive, User.Id userId) {
            return new AccountEntity(
                    accountId,
                    accountNumber,
                    balance,
                    currency,
                    address,
                    isActive,
                    userId
            );
        }
    }
}
