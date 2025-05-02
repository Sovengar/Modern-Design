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
import jakarta.persistence.Version;
import jonathan.modern_design._common.AuditingColumns;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.user.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "accounts", schema = "md")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class AccountEntity extends AuditingColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "MD.ACCOUNTS_SQ", allocationSize = 1)
    private Long accountId; //Can't use microType with a sequence
    private String accountNumber;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Account.Status status;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private String address;
    @Embedded
    private User.Id userId;
    @Version
    private Integer version;

    public Account toDomain() {
        return new Account(Account.Id.of(accountId), AccountAccountNumber.of(accountNumber), status, AccountMoney.of(balance, currency), AccountAddress.of(address), userId);
    }

    public void updateFrom(Account account) {
        this.accountId = account.accountId().id();
        this.accountNumber = account.accountAccountNumber().accountNumber();
        this.balance = account.money().balance();
        this.currency = account.money().currency();
        this.address = account.address().toString();
        this.status = account.status();
        this.userId = account.userId();
    }

    @PrePersist
    public void prePersist() {
        this.status = Account.Status.ACTIVE;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static AccountEntity create(Account account) {
            //If we start to use id from the client, we could assign the id directly
            var accountId = nonNull(account.accountId()) ? account.accountId().id() : null;
            var accountNumber = account.accountAccountNumber().accountNumber();
            var active = Account.Status.ACTIVE;
            var balance = account.money().balance();
            var currency = account.money().currency();
            var address = account.address().toString();
            var userId = account.userId();
            var version = 0;

            return new AccountEntity(accountId, accountNumber, active, balance, currency, address, userId, version);
        }

        public static AccountEntity create(Long accountId, String accountNumber, BigDecimal balance, Currency currency, String address, User.Id userId) {
            return new AccountEntity(
                    accountId,
                    accountNumber,
                    Account.Status.ACTIVE,
                    balance,
                    currency,
                    address,
                    userId,
                    0
            );
        }
    }
}
