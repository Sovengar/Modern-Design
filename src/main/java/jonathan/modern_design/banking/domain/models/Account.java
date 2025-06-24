package jonathan.modern_design.banking.domain.models;

import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design._shared.tags.AggregateRoot;
import jonathan.modern_design._shared.tags.MicroType;
import jonathan.modern_design.banking.api.events.AccountCreated;
import jonathan.modern_design.banking.domain.events.AccountActivated;
import jonathan.modern_design.banking.domain.events.AccountDeactivated;
import jonathan.modern_design.banking.domain.events.MoneyDeposited;
import jonathan.modern_design.banking.domain.events.MoneyWithdrawed;
import jonathan.modern_design.banking.domain.events.NewAccountNumberGenerated;
import jonathan.modern_design.banking.domain.exceptions.AccountIsAlreadyActiveException;
import jonathan.modern_design.banking.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.banking.domain.policies.AccountNumberGenerator;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AggregateRoot
public final class Account {
    private final List<Object> domainEvents = new ArrayList<>();
    private final Id accountId; //No behavior, but keep it to make debugging easier
    private AccountNumber accountNumber;
    private Status status;
    private Money money;

    public Account(AccountEntity accountEntity) {
        this.accountId = Id.of(accountEntity.getId());
        this.accountNumber = AccountNumber.of(accountEntity.getAccountNumber());
        this.money = Money.of(accountEntity.getBalance(), accountEntity.getCurrency());
        this.status = accountEntity.getStatus();
    }

    private Account(AccountNumber accountNumber, Status status, Money money) {
        this.accountId = null;
        this.accountNumber = accountNumber;
        this.status = status;
        this.money = money;

        new AccountCreated(accountNumber.getAccountNumber());
    }

    /**
     * CRUD method, prefer usecase methods like moveToAnotherPlace to update the address only.
     * If we keep making the method more generic, the method will grow complex.
     * Logic will be dispersed since the client now has the burden to provide the right fields to support his need.
     */
    public void genericUpdate(AccountNumber accountNumber, Money money, Status status) {
        this.accountNumber = accountNumber;
        this.money = money;
        this.status = status;
    }

    //Double Dispatch
    public void generateNewAccountNumber(AccountNumberGenerator generator) {
        this.accountNumber = AccountNumber.of(generator.generate(this));
        this.domainEvents.add(new NewAccountNumberGenerated(this.accountNumber.getAccountNumber()));
    }

    public void deposit(Money money) {
        this.money = this.money.add(money);
        this.domainEvents.add(new MoneyDeposited(money, this.accountNumber.getAccountNumber()));
    }

    public void withdrawal(Money money) {
        this.money = this.money.subtract(money);
        this.domainEvents.add(new MoneyWithdrawed(money, this.accountNumber.getAccountNumber()));
    }

    public void deactivate() {
        if (this.status == Status.INACTIVE) throw new AccountIsInactiveException(this.accountNumber.getAccountNumber());
        this.status = Status.INACTIVE;
        this.domainEvents.add(new AccountDeactivated(this.accountNumber.getAccountNumber()));
    }

    public void activate() {
        if (this.status == Status.ACTIVE) throw new AccountIsAlreadyActiveException(this.accountNumber.getAccountNumber());
        this.status = Status.ACTIVE;
        this.domainEvents.add(new AccountActivated(this.accountNumber.getAccountNumber()));
    }

    public List<Object> moveEventsToDataModel() {
        var copy = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return copy;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    public record Id(Long id) implements MicroType {
        public static Id of(Long id) {
            return new Id(id);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static Account create(AccountNumber accountNumber, Money money) {
            return new Account(requireNonNull(accountNumber), Status.ACTIVE, requireNonNull(money));
        }
    }
}
