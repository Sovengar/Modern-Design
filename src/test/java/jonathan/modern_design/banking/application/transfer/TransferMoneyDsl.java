package jonathan.modern_design.banking.application.transfer;

import com.github.javafaker.Faker;
import jonathan.modern_design._shared.domain.vo.Money;
import jonathan.modern_design.banking.application.TransferMoney;
import jonathan.modern_design.banking.domain.AccountDsl;

import java.math.BigDecimal;

import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;

//It is more an ObjectMother than Dsl, migrate to Dsl if needed.
public class TransferMoneyDsl {
    public static final Faker faker = new Faker();

    public static TransferMoney.Command fromAccountToAccountWithAmount(String sourceAccountId, String targetAccountId, Money money) {
        return new TransferMoney.Command(sourceAccountId, targetAccountId, money.getBalance(), money.getCurrency());
    }

    public static TransferMoney.Command transactionWithAmount(Money money) {
        return fromAccountToAccountWithAmount(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER, AccountDsl.DEFAULT_TARGET_ACCOUNT_NUMBER, money);
    }

    public static TransferMoney.Command insufficientFundsTransaction() {
        return fromAccountToAccountWithAmount(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER, AccountDsl.DEFAULT_TARGET_ACCOUNT_NUMBER, Money.of(BigDecimal.valueOf(100.0), EUR));
    }

    public static TransferMoney.Command negativeAmountTransaction() {
        return fromAccountToAccountWithAmount(AccountDsl.DEFAULT_SOURCE_ACCOUNT_NUMBER, AccountDsl.DEFAULT_TARGET_ACCOUNT_NUMBER, Money.of(BigDecimal.valueOf(-100), EUR));
    }
}
