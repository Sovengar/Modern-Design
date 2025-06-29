package jonathan.modern_design._dsl;

import jonathan.modern_design.__config.Stub;
import jonathan.modern_design._shared.domain.Country;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.application.create_account.CreateAccount;
import jonathan.modern_design.banking.application.create_account.CreateAccountRequest;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.domain.vo.AccountNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jonathan.modern_design._dsl.UserStub.VALID_PASSWORD;
import static jonathan.modern_design._shared.domain.Currency.EUR;
import static jonathan.modern_design._shared.domain.Currency.USD;

public class AccountStub extends Stub {

    public static final String DEFAULT_COUNTRY = "ES";
    public static final Country SPAIN = new Country(DEFAULT_COUNTRY, "Spain");
    public static String sourceAccountId = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static String targetAccountId = "0db3c62f-c978-41ad-95a9-9230aa85593f";
    public static String username = faker.name().username();
    public static String email = faker.internet().emailAddress();
    public static String fullName = faker.name().fullName();
    public static CreateAccount.Command.Address address = new CreateAccount.Command.Address("Rupert", "Alicante", "Comunidad Valenciana", "033187", DEFAULT_COUNTRY);
    public static AccountHolderAddress ahAddress = AccountHolderAddress.of(address.street(), address.city(), address.state(), address.zipCode(), SPAIN);
    public static String personalId = "48732228A";
    public static List<String> phoneNumbers = List.of(faker.phoneNumber().phoneNumber());
    public static LocalDate birthdate = LocalDate.of(1990, 1, 1);

    public static class AccountMother {
        public static Account sourceAccountWithBalance(double balance) {
            return builder(sourceAccountId, Money.of(BigDecimal.valueOf(balance), EUR), true, AccountHolderMother.randomAccountHolder());
        }

        public static Account targetAccountWithBalance(double balance) {
            return builder(targetAccountId, Money.of(BigDecimal.valueOf(balance), EUR), true, AccountHolderMother.randomAccountHolder());
        }

        public static Account sourceAccountEmpty() {
            return builder(sourceAccountId, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderMother.randomAccountHolder());
        }

        public static Account sourceAccountInactive() {
            return builder(sourceAccountId, Money.of(BigDecimal.ZERO, EUR), false, AccountHolderMother.randomAccountHolder());
        }

        public static Account targetAccountEmpty() {
            return builder(targetAccountId, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderMother.randomAccountHolder());
        }

        public static Account targetAccountInactive() {
            return builder(targetAccountId, Money.of(BigDecimal.ZERO, EUR), false, AccountHolderMother.randomAccountHolder());
        }

        public static Account targetAccountWithDifferentCurrency() {
            return builder(targetAccountId, Money.of(BigDecimal.ZERO, USD), true, AccountHolderMother.randomAccountHolder());
        }

        public static Account accountWithUserId(UUID userId) {
            return builder(sourceAccountId, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderMother.accountHolder(UUID.randomUUID(), userId));
        }

        private static Account builder(String accountId, Money money, boolean isActive, AccountHolder accountHolder) {
            var accountNumber = AccountNumber.of(accountId);
            var account = Account.Factory.create(accountNumber, money, accountHolder);

            if (!isActive) {
                account.deactivate();
            }

            return account;
        }
    }

    public static class AccountHolderMother {
        public static AccountHolder randomAccountHolder() {
            return accountHolder(UUID.randomUUID(), UUID.randomUUID());
        }

        public static AccountHolder accountHolder(UUID accountHolderId, UUID userId) {
            return AccountHolder.create(accountHolderId, Optional.of(fullName), personalId, ahAddress, birthdate, phoneNumbers, userId);
        }
    }

    public static class CreateAccountMother extends Stub {
        public static CreateAccount.Command createAccountCommand() {
            return createAccountCommand(EUR.getCode());
        }

        public static CreateAccount.Command createAccountCommand(final String currencyCode) {
            return new CreateAccount.Command(Optional.of(fullName), email, username, address, VALID_PASSWORD, currencyCode, phoneNumbers, birthdate, personalId);
        }

        public static CreateAccountRequest createAccountRequest(final String currencyCode) {
            return new CreateAccountRequest(fullName, email, username, address, VALID_PASSWORD, currencyCode, phoneNumbers, birthdate, personalId);
        }
    }

    public static class TransferMoneyMother extends Stub {
        public static TransferMoney.Command fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, Money money) {
            return new TransferMoney.Command(sourceAccountId, targetAccountId, money.getBalance(), money.getCurrency());
        }

        public static TransferMoney.Command transactionWithAmount(Money money) {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, money);
        }

        public static TransferMoney.Command insufficientFundsTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, Money.of(BigDecimal.valueOf(100.0), EUR));
        }

        public static TransferMoney.Command negativeAmountTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, Money.of(BigDecimal.valueOf(-100), EUR));
        }
    }
}
