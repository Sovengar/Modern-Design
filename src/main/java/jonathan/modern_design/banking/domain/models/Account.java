package jonathan.modern_design.banking.domain.models;

import jonathan.modern_design._shared.tags.AggregateRoot;
import jonathan.modern_design._shared.tags.MicroType;
import jonathan.modern_design._shared.vo.AccountMoney;
import jonathan.modern_design.banking.domain.exceptions.AccountIsAlreadyActiveException;
import jonathan.modern_design.banking.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.banking.domain.vo.AccountNumber;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AggregateRoot
public final class Account {
    private Id accountId;
    private AccountNumber accountNumber;
    private Status status;
    private AccountMoney money;

    //TODO THIS MAKES 0 SENSE, EXTRACT FIELDS THAT HAS NO LOGIC ASSOCIATED
    public Account(AccountEntity accountEntity) {
        this.accountId = Id.of(accountEntity.getAccountId());
        this.accountNumber = AccountNumber.of(accountEntity.getAccountNumber());
        this.money = AccountMoney.of(accountEntity.getBalance(), accountEntity.getCurrency());
        this.status = accountEntity.getStatus();
    }

    /**
     * CRUD method, prefer usecase methods like moveToAnotherPlace to update the address only.
     * If we keep making the method more generic, the method will grow complex.
     * Logic will be dispersed since the client now has the burden to provide the right fields to support his need.
     */
    public void genericUpdate(AccountNumber accountNumber, AccountMoney money, Status status) {
        this.accountNumber = accountNumber;
        this.money = money;
        this.status = status;
    }

    public void generateNewAccountNumber() {
        //TODO TRY TO USE DOBLE DISPATCH
    }

    public void deposit(AccountMoney money) {
        this.money = this.money.add(money);
    }

    public void withdrawal(AccountMoney money) {
        this.money = this.money.subtract(money);
    }

    public void deactivate() {
        if (this.status == Status.INACTIVE) throw new AccountIsInactiveException(this.accountNumber.getAccountNumber());
        this.status = Status.INACTIVE;
    }

    public void activate() {
        if (this.status == Status.ACTIVE) throw new AccountIsAlreadyActiveException(this.accountNumber.getAccountNumber());
        this.status = Status.ACTIVE;
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
        public static Account create(AccountNumber accountNumber, AccountMoney money) {

            return new Account(
                    null,
                    requireNonNull(accountNumber),
                    Status.ACTIVE,
                    requireNonNull(money));
        }
    }

}
