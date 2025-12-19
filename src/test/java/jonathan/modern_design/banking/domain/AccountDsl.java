package jonathan.modern_design.banking.domain;

import com.github.javafaker.Faker;
import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.application.create_account.CreateAccountCommand;
import jonathan.modern_design.banking.domain.models.Account;
import jonathan.modern_design.banking.domain.models.AccountHolder;
import jonathan.modern_design.banking.domain.vo.AccountHolderAddress;
import jonathan.modern_design.banking.domain.vo.AccountNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static jonathan.modern_design._shared.domain.catalogs.Currency.USD;

public class AccountDsl {
    public static final Faker faker = new Faker();

    public static final String DEFAULT_COUNTRY = "ES";
    public static final Country SPAIN = new Country(DEFAULT_COUNTRY, "Spain");
    public static final String DEFAULT_ACCOUNT_NUMBER = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static final String DEFAULT_SOURCE_ACCOUNT_NUMBER = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static final String DEFAULT_TARGET_ACCOUNT_NUMBER = "0db3c62f-c978-41ad-95a9-9230aa85593f";
    public static final String username = faker.name().username();
    public static final String email = faker.internet().emailAddress();
    public static final String fullName = faker.name().fullName();
    public static final CreateAccountCommand.Address address = new CreateAccountCommand.Address("Rupert", "Alicante", "Comunidad Valenciana", "033187", DEFAULT_COUNTRY);
    public static final AccountHolderAddress ahAddress = AccountHolderAddress.of(AccountHolderAddress.StreetType.AVENUE, address.street(), address.city(), address.state(), address.zipCode(), SPAIN);
    public static final String personalId = "48732228A";
    public static final List<String> phoneNumbers = List.of(faker.phoneNumber().cellPhone());
    public static final LocalDate birthdate = LocalDate.of(1990, 1, 1);

    public static Account givenAnAccountWithMoney(Money money, String accountNumber) {
        return builder(accountNumber, money, true, AccountHolderDsl.randomAccountHolder());
    }

    public static Account givenARandomAccountWithBalance(double balance) {
        return givenAnAccountWithBalance(balance, UUID.randomUUID().toString());
    }

    public static Account givenAnAccountWithBalance(double balance, String accountNumber) {
        return builder(accountNumber, Money.of(balance, EUR), true, AccountHolderDsl.randomAccountHolder());
    }

    public static Account givenAnAccountWithBalance(double balance) {
        return givenAnAccountWithBalance(balance, DEFAULT_SOURCE_ACCOUNT_NUMBER);
    }

    public static Account givenAnEmptyAccount() {
        return builder(DEFAULT_SOURCE_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderDsl.randomAccountHolder());
    }

    public static Account givenAnInactiveAccount() {
        return builder(DEFAULT_SOURCE_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, EUR), false, AccountHolderDsl.randomAccountHolder());
    }

    public static Account givenAntargetAccountWithDifferentCurrency() {
        return builder(DEFAULT_TARGET_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, USD), true, AccountHolderDsl.randomAccountHolder());
    }

    public static Account givenAnAccountWithUserId(UUID userId) {
        return builder(DEFAULT_SOURCE_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderDsl.accountHolder(UUID.randomUUID(), userId));
    }

    public static Account emptyTargetAccount() {
        return builder(DEFAULT_TARGET_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, EUR), true, AccountHolderDsl.randomAccountHolder());
    }

    public static Account inactiveTargetAccount() {
        return builder(DEFAULT_TARGET_ACCOUNT_NUMBER, Money.of(BigDecimal.ZERO, EUR), false, AccountHolderDsl.randomAccountHolder());
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

