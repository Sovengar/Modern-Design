package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.__config.Stub;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;
import com.jonathan.modern_design.account_module.dtos.AccountCreatorCommand;
import com.jonathan.modern_design.account_module.dtos.TransferMoneyCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.jonathan.modern_design._fake_data.UserStub.DEFAULT_COUNTRY;
import static com.jonathan.modern_design._fake_data.UserStub.VALID_PASSWORD;
import static com.jonathan.modern_design._fake_data.UserStub.normalUser;
import static com.jonathan.modern_design._shared.Currency.EURO;
import static com.jonathan.modern_design._shared.Currency.US_DOLLAR;

public class AccountStub extends Stub {

    public static String sourceAccountId = "1e95e7f2-1b5b-4049-a37e-44385b3533e3";
    public static String targetAccountId = "0db3c62f-c978-41ad-95a9-9230aa85593f";

    public static class AccountMother {
        public static Account sourceAccountWithBalance(double balance) {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.valueOf(balance), EURO), true);
        }

        public static Account targetAccountWithBalance(double balance) {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.valueOf(balance), EURO), true);
        }

        /// //////////////

        public static Account sourceAccountEmpty() {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.ZERO, EURO), true);
        }

        public static Account sourceAccountInactive() {
            return builder(sourceAccountId, AccountMoney.of(BigDecimal.ZERO, EURO), false);
        }

        public static Account targetAccountEmpty() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, EURO), true);
        }

        public static Account targetAccountInactive() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, EURO), false);
        }

        public static Account targetAccountWithDifferentCurrency() {
            return builder(targetAccountId, AccountMoney.of(BigDecimal.ZERO, US_DOLLAR), true);
        }

        private static Account builder(String accountId, AccountMoney money, boolean isActive) {
            return Account.builder()
                    .accountId(null)
                    .accountNumber(AccountNumber.of(accountId))
                    .money(money)
                    .address(AccountAddress.of("street", "city", "state", "zipCode"))
                    .userId(normalUser().getUuid())
                    .dateOfLastTransaction(LocalDateTime.now())
                    .active(isActive)
                    .build();
        }
    }

    public static class CreateAccountMother extends Stub {
        public static AccountCreatorCommand createAccountCommandWithInvalidData() {
            var username = "Account Name";
            var email = "z3u1E@example.com";
            var realname = "John Doe";
            var currency = EURO.getCode();
            var password = "123456";
            var country = "XXX";
            var address = "address";
            return new AccountCreatorCommand(realname, email, username, address, password, country, currency);
        }

        public static AccountCreatorCommand createAccountCommandWithValidData() {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new AccountCreatorCommand(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY.code(), EURO.getCode());
        }

        public static AccountCreatorCommand randomAccountWithCurrency(Currency currency) {
            var username = faker.name().username();
            var email = faker.internet().emailAddress();
            var realname = faker.name().fullName();
            var address = "street, city, state, zipCode";
            return new AccountCreatorCommand(realname, email, username, address, VALID_PASSWORD, DEFAULT_COUNTRY.code(), currency.getCode());
        }
    }

    public static class TransferMoneyMother extends Stub {
        public static TransferMoneyCommand fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, AccountMoney money) {
            return new TransferMoneyCommand(sourceAccountId, targetAccountId, money.getAmount(), money.getCurrency());
        }

        public static TransferMoneyCommand transactionWithAmount(AccountMoney money) {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, money);
        }

        public static TransferMoneyCommand insufficientFundsTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
        }

        public static TransferMoneyCommand negativeAmountTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(-100), EURO));
        }
    }
}
