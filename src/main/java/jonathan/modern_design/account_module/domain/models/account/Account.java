package jonathan.modern_design.account_module.domain.models.account;

import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsAlreadyActiveException;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.user.domain.models.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Builder //For mapper and tests only //TODO TRY TO DELETE
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AggregateRoot
public final class Account {
    private Id accountId;
    private AccountAccountNumber accountAccountNumber;
    private Status status;
    private AccountMoney money;
    private AccountAddress address;
    private User.Id userId;

    //TODO THIS MAKES 0 SENSE, EXTRACT FIELDS THAT HAS NO LOGIC ASSOCIATED
    public Account(AccountEntity accountEntity) {
        this.accountId = Id.of(accountEntity.getAccountId());
        this.accountAccountNumber = AccountAccountNumber.of(accountEntity.getAccountNumber());
        this.money = AccountMoney.of(accountEntity.getBalance(), accountEntity.getCurrency());
        this.address = AccountAddress.of(accountEntity.getAddress());
        this.userId = accountEntity.getUserId();
        this.status = accountEntity.getStatus();
    }

    public void updateCRUD(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, Status status, User.Id userId) {
        this.accountAccountNumber = accountAccountNumber;
        this.money = money;
        this.address = address;
        this.status = status;
        this.userId = userId;
    }

    public void deposit(AccountMoney money) {
        this.money = this.money.add(money);
    }

    public void withdrawal(AccountMoney money) {
        this.money = this.money.subtract(money);
    }

    public void deactivate() {
        if (this.status == Status.INACTIVE) throw new AccountIsInactiveException(this.accountAccountNumber.getAccountNumber());
        this.status = Status.INACTIVE;
    }

    public void activate() {
        if (this.status == Status.ACTIVE) throw new AccountIsAlreadyActiveException(this.accountAccountNumber.getAccountNumber());
        this.status = Status.ACTIVE;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    public record Id(Long id) {
        public static Id of(Long id) {
            return new Id(id);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static Account create(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, User.Id userId) {

            return new Account(
                    null,
                    requireNonNull(accountAccountNumber),
                    Status.ACTIVE,
                    requireNonNull(money),
                    requireNonNull(address),
                    requireNonNull(userId)
            );
        }
    }

}
