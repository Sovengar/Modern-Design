package jonathan.modern_design.account_module.domain.models.account;

import jonathan.modern_design._common.annotations.AggregateRoot;
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
    Id accountId;
    AccountAccountNumber accountAccountNumber;
    AccountMoney money;
    AccountAddress address;
    User.Id userId;
    boolean active;

    //TODO THIS MAKES 0 SENSE, EXTRACT FIELDS THAT HAS NO LOGIC ASSOCIATED
    public Account(AccountEntity accountEntity) {
        this.accountId = Id.of(accountEntity.accountId());
        this.accountAccountNumber = AccountAccountNumber.of(accountEntity.accountNumber());
        this.money = AccountMoney.of(accountEntity.balance(), accountEntity.currency());
        this.address = AccountAddress.of(accountEntity.address());
        this.userId = accountEntity.userId();
        this.active = accountEntity.active();
    }

    //TODO USE THE ENTITY CONSTRUCTOR?
    public static Account updateCRUD(Account account, AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, boolean isActive, User.Id userId) {
        return new Account(account.accountId(), accountAccountNumber, money, address, userId, isActive);
    }

    public void deposit(AccountMoney money) {
        this.money = this.money.add(money);
    }

    public void withdrawal(AccountMoney money) {
        this.money = this.money.subtract(money);
    }

    public void deactivate() {
        if (!this.active) throw new AccountIsInactiveException(this.accountAccountNumber.accountNumber());
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public record Id(Long id) {
        public static Id of(Long id) {
            return new Id(id);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Factory {
        public static Account create(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, User.Id userId) {
            var isActive = true;

            return new Account(
                    null,
                    requireNonNull(accountAccountNumber),
                    requireNonNull(money),
                    requireNonNull(address),
                    requireNonNull(userId),
                    isActive);
        }
    }
}
