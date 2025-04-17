package jonathan.modern_design._fake_data;

import jonathan.modern_design.__config.Stub;
import jonathan.modern_design._shared.Currency;
import jonathan.modern_design.account_module.domain.Account;
import jonathan.modern_design.account_module.domain.vo.AccountAccountNumber;
import jonathan.modern_design.account_module.domain.vo.AccountAddress;
import jonathan.modern_design.account_module.domain.vo.AccountMoney;
import jonathan.modern_design.account_module.dtos.CreateAccountCommand;
import jonathan.modern_design.account_module.dtos.TransferMoneyCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
            return Account.builder()
                    .accountId(null)
                    .accountAccountNumber(AccountAccountNumber.of(accountId))
                    .money(money)
                    .address(AccountAddress.of("street", "city", "state", "zipCode"))
                    .userId(normalUser().uuid())
                    .dateOfLastTransaction(LocalDateTime.now())
                    .active(isActive)
                    .build();
        }
    }

    public static class CreateAccountMother extends Stub {
        public static CreateAccountCommand createAccountCommandWithInvalidData() {
            var username = "Account Name";
            var email = "z3u1E@example.com";
            var realname = "John Doe";
            var currency = EUR.description();
            var password = "123456";
            var country = "XXX";
            var address = "address";
            return new CreateAccountCommand(realname, email, username, address, password, country, currency);
        }

        public static CreateAccountCommand createAccountCommandWithValidData() {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new CreateAccountCommand(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY.code(), EUR.description());
        }

        public static CreateAccountCommand randomAccountWithCurrency(Currency currency) {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new CreateAccountCommand(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY.code(), currency.description());
        }
    }

    public static class TransferMoneyMother extends Stub {
        public static TransferMoneyCommand fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, AccountMoney money) {
            return new TransferMoneyCommand(sourceAccountId, targetAccountId, money.amount(), money.currency());
        }

        public static TransferMoneyCommand transactionWithAmount(AccountMoney money) {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, money);
        }

        public static TransferMoneyCommand insufficientFundsTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(100.0), EUR));
        }

        public static TransferMoneyCommand negativeAmountTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(-100), EUR));
        }
    }
}
