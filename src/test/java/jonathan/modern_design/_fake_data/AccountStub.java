package jonathan.modern_design._fake_data;

import jonathan.modern_design.__config.Stub;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.banking.application.CreateAccount;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.domain.models.account.Account;
import jonathan.modern_design.banking.domain.models.account.vo.AccountAddress;
import jonathan.modern_design.banking.domain.models.account.vo.AccountMoney;
import jonathan.modern_design.banking.domain.models.account.vo.AccountNumber;

import java.math.BigDecimal;

import static jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;
import static jonathan.modern_design._fake_data.UserStub.VALID_PASSWORD;
import static jonathan.modern_design._fake_data.UserStub.normalUser;
import static jonathan.modern_design._shared.Currency.EUR;
import static jonathan.modern_design._shared.Currency.USD;

public class AccountStub extends Stub {

    public static String sourceAccountId = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static String targetAccountId = "0db3c62f-c978-41ad-95a9-9230aa85593f";

    public static class AccountMother {
        public static Account sourceAccountWithBalance(double balance) {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.valueOf(balance), EUR), true);
        }

        public static Account targetAccountWithBalance(double balance) {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.valueOf(balance), EUR), true);
        }

        /// //////////////

        public static Account sourceAccountEmpty() {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.ZERO, EUR), true);
        }

        public static Account sourceAccountInactive() {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.ZERO, EUR), false);
        }

        public static Account targetAccountEmpty() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, EUR), true);
        }

        public static Account targetAccountInactive() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, EUR), false);
        }

        public static Account targetAccountWithDifferentCurrency() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, USD), true);
        }

        private static Account builder(String accountId, AccountMoney money, boolean isActive) {
            var accountNumber = AccountNumber.of(accountId);
            var address = AccountAddress.of("street", "city", "state", "zipCode");
            var userId = normalUser().getId();

            var account = Account.Factory.create(accountNumber, money, address, userId);

            if (!isActive) {
                account.deactivate();
            }

            return account;
        }
    }

    public static class CreateAccountMother extends Stub {
        public static CreateAccount.Command createAccountCommandWithInvalidData() {
            var username = "Account Name";
            var email = "z3u1E@example.com";
            var realname = "John Doe";
            var currency = EUR.getDescription();
            var password = "123456";
            var country = "XXX";
            var address = "address";
            return new CreateAccount.Command(realname, email, username, address, password, country, currency);
        }

        public static CreateAccount.Command createAccountCommandWithValidData() {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new CreateAccount.Command(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY, EUR.getCode());
        }

        public static CreateAccount.Command randomAccountWithCurrency(Currency currency) {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new CreateAccount.Command(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY, currency.getCode());
        }
    }

    public static class TransferMoneyMother extends Stub {
        public static TransferMoney.Command fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, AccountMoney money) {
            return new TransferMoney.Command(sourceAccountId, targetAccountId, money.getBalance(), money.getCurrency());
        }

        public static TransferMoney.Command transactionWithAmount(AccountMoney money) {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, money);
        }

        public static TransferMoney.Command insufficientFundsTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
        }

        public static TransferMoney.Command negativeAmountTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(-100), EUR));
        }
    }
}
