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
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountNumber;
import jonathan.modern_design.user.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder //Allowed because is a class without biz logic, use only for mapping or testing purposes
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
        return new Account(Account.Id.of(accountId), AccountNumber.of(accountNumber), status, AccountMoney.of(balance, currency), AccountAddress.of(address), userId);
    }

    public void updateFrom(Account account) {
        this.accountId = account.getAccountId().id();
        this.accountNumber = account.getAccountNumber().getAccountNumber();
        this.balance = account.getMoney().getBalance();
        this.currency = account.getMoney().getCurrency();
        this.address = account.getAddress().toString();
        this.status = account.getStatus();
        this.userId = account.getUserId();
    }

    @PrePersist
    public void prePersist() {
        this.status = Account.Status.ACTIVE;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static AccountEntity create(Account account) {
            //If we start to use id from the client, we could assign the id directly
            var accountId = nonNull(account.getAccountId()) ? account.getAccountId().id() : null;
            var accountNumber = account.getAccountNumber().getAccountNumber();
            var active = Account.Status.ACTIVE;
            var balance = account.getMoney().getBalance();
            var currency = account.getMoney().getCurrency();
            var address = account.getAddress().toString();
            var userId = account.getUserId();
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
