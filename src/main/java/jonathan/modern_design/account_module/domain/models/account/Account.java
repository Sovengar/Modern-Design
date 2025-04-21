package jonathan.modern_design.account_module.domain.models.account;

import jonathan.modern_design._common.annotations.AggregateRoot;
import jonathan.modern_design.account_module.domain.exceptions.AccountIsInactiveException;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountId;
import jonathan.modern_design.account_module.domain.models.account.vo.AccountMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jonathan.modern_design.user.domain.models.User.UserId;

@Builder //For mapper and tests only //TODO DELETE?
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AggregateRoot
public final class Account {
    private final List<Transaction> transactions = new ArrayList<>();
    AccountId accountId;
    AccountAccountNumber accountAccountNumber;
    AccountMoney money;
    AccountAddress address;
    UserId userId;
    boolean active;

    //TODO THIS MAKES 0 SENSE, EXTRACT FIELDS THAT HAS NO LOGIC ASSOCIATED
    public Account(AccountEntity accountEntity) {
        this.accountId = new AccountId(accountEntity.accountId());
        this.accountAccountNumber = AccountAccountNumber.of(accountEntity.accountNumber());
        this.money = AccountMoney.of(accountEntity.balance(), accountEntity.currency());
        this.address = AccountAddress.of(accountEntity.address());
        this.userId = accountEntity.userId();
        this.active = accountEntity.active();
    }

    public static Account create(AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, UserId userId) {
        var isActive = true;

        return new Account(
                null,
                requireNonNull(accountAccountNumber),
                requireNonNull(money),
                requireNonNull(address),
                requireNonNull(userId),
                isActive);
    }

    //TODO USE THE ENTITY CONSTRUCTOR?
    public static Account updateCRUD(Account account, AccountAccountNumber accountAccountNumber, AccountMoney money, AccountAddress address, boolean isActive, UserId userId) {
        Account updated = new Account(account.accountId(), accountAccountNumber, money, address, userId, isActive);
        updated.transactions.addAll(account.transactions);
        return updated;
    }

    public void deposit(AccountMoney money) {
        this.money = this.money.add(money);
        this.transactions.add(Transaction.deposit(money, this.accountAccountNumber.accountNumber()));
    }

    public void withdrawal(AccountMoney money) {
        this.money = this.money.substract(money);
        this.transactions.add(Transaction.withdrawal(money, this.accountAccountNumber.accountNumber()));
    }

    public void transferTo(AccountAccountNumber destination, AccountMoney money) {
        this.money = this.money.substract(money);
        var transaction = Transaction.transfer(money, this.accountAccountNumber.accountNumber(), destination.accountNumber());
        this.transactions.add(transaction);
    }

    public void receiveTransferFrom(AccountAccountNumber source, AccountMoney money) {
        this.money = this.money.add(money);
        var transaction = Transaction.transfer(money, source.accountNumber(), this.accountAccountNumber.accountNumber());
        this.transactions.add(transaction);
    }

    public void deactivate() {
        if (!this.active) throw new AccountIsInactiveException(this.accountAccountNumber.accountNumber());
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public List<Transaction> transactionHistory() {
        return List.copyOf(transactions);
    }

    public Optional<LocalDateTime> dateOfLastTransaction() {
        return transactions.stream()
                .map(Transaction::transactionDate)
                .max(Comparator.naturalOrder());
    }
}
