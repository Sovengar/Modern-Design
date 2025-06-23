package jonathan.modern_design.banking.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.domain.Currency;
import jonathan.modern_design._shared.infra.BaseAggregateRoot;
import jonathan.modern_design.banking.api.events.AccountSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "accounts", schema = "banking")
@Getter
@NoArgsConstructor(access = PACKAGE) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
@Builder //Allowed because is a class without biz logic, use only for mapping or testing purposes
public class AccountEntity extends BaseAggregateRoot<AccountEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_SQ")
    @SequenceGenerator(name = "ACCOUNTS_SQ", sequenceName = "BANKING.ACCOUNTS_SQ", allocationSize = 1)
    private Long accountId; //Can't use microType with a sequence
    private String accountNumber;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private Account.Status status;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    @OneToOne
    @JoinColumn(name = "account_holder_id")
    private AccountHolder accountHolder;

    //Doesn't need to be a static method, coupling is managed, if it needs to be changed, there will be 3-4 occurrences only
    public AccountEntity(Account account) {
        //If we start to use id from the client, we could assign the id directly
        this.accountId = nonNull(account.getAccountId()) ? account.getAccountId().id() : null;
        this.accountNumber = account.getAccountNumber().getAccountNumber();
        this.status = Account.Status.ACTIVE;
        this.balance = account.getMoney().getBalance();
        this.currency = account.getMoney().getCurrency();
        moveEventsFrom(account);
    }

    public void updateFrom(Account account) {
        this.accountId = account.getAccountId().id();
        this.accountNumber = account.getAccountNumber().getAccountNumber();
        this.balance = account.getMoney().getBalance();
        this.currency = account.getMoney().getCurrency();
        this.status = account.getStatus();
        moveEventsFrom(account);
    }

    private void moveEventsFrom(Account account) {
        var domainEvents = account.moveEventsToDataModel();
        domainEvents.forEach(this::registerEvent);
        this.registerEvent(new AccountSnapshot(account.getAccountNumber().getAccountNumber(), account.getMoney(), account.getStatus()));
    }

    @PrePersist
    public void prePersist() {
        this.status = Account.Status.ACTIVE;
    }
}
