package com.jonathan.modern_design._fake_data;

import com.jonathan.modern_design.__config.Stub;
import com.jonathan.modern_design._shared.Currency;
import com.jonathan.modern_design.account_module.application.CreateAccountUseCase;
import com.jonathan.modern_design.account_module.application.TransferMoneyUseCase;
import com.jonathan.modern_design.account_module.domain.model.Account;
import com.jonathan.modern_design.account_module.domain.model.AccountAddress;
import com.jonathan.modern_design.account_module.domain.model.AccountMoney;
import com.jonathan.modern_design.account_module.domain.model.AccountNumber;

import java.math.BigDecimal;

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
                    .accountNumber(AccountNumber.of(accountId))
                    .money(money)
                    .address(AccountAddress.of("street", "city", "state", "zipCode"))
                    .userId(normalUser().getUuid())
                    .active(isActive).build();
        }
    }

    public static class CreateAccountMother extends Stub {
        public static CreateAccountUseCase.CreateAccountCommand createAccountCommandWithInvalidData() {
            return CreateAccountUseCase.CreateAccountCommand.builder()
                    .username("Account Name")
                    .email("z3u1E@example.com")
                    .realname("John Doe")
                    .currency(EURO.getCode())
                    .password("123456")
                    .country("XXX")
                    .build();
        }

        public static CreateAccountUseCase.CreateAccountCommand createAccountCommandWithValidData() {
            return CreateAccountUseCase.CreateAccountCommand.builder()
                    .username(faker.name().username())
                    .email(faker.internet().emailAddress())
                    .realname(faker.name().fullName())
                    .address("street, city, state, zipCode")
                    .currency(EURO.getCode())
                    .password(VALID_PASSWORD)
                    .country(DEFAULT_COUNTRY.code())
                    .build();
        }

        public static CreateAccountUseCase.CreateAccountCommand randomAccountWithCurrency(Currency currency) {
            return CreateAccountUseCase.CreateAccountCommand.builder()
                    .username(faker.name().username())
                    .email(faker.internet().emailAddress())
                    .realname(faker.name().fullName())
                    .address("street, city, state, zipCode")
                    .currency(currency.getCode())
                    .password(VALID_PASSWORD)
                    .country(DEFAULT_COUNTRY.code())
                    .build();
        }
    }

    public static class TransferMoneyMother extends Stub {
        public static TransferMoneyUseCase.TransferMoneyCommand fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, AccountMoney money) {
            return new TransferMoneyUseCase.TransferMoneyCommand(sourceAccountId, targetAccountId, money.getAmount(), money.getCurrency());
        }

        public static TransferMoneyUseCase.TransferMoneyCommand transactionWithAmount(AccountMoney money) {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, money);
        }

        public static TransferMoneyUseCase.TransferMoneyCommand insufficientFundsTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(100.0), EURO));
        }

        public static TransferMoneyUseCase.TransferMoneyCommand negativeAmountTransaction() {
            return fromAccountToAccountWithAmount(sourceAccountId, targetAccountId, AccountMoney.of(BigDecimal.valueOf(-100), EURO));
        }
    }
}
