package jonathan.modern_design.banking.application.create_account;

import jonathan.modern_design.banking.domain.AccountStub;

import java.util.Optional;

import static jonathan.modern_design._shared.domain.catalogs.Currency.EUR;
import static jonathan.modern_design.auth.domain.UserDsl.VALID_PASSWORD;

//It is more an ObjectMother than Dsl, migrate to Dsl if needed.
public class CreateAccountDsl {
    public static CreateAccountCommand withValidData() {
        return withCurrency(EUR.getCode());
    }

    public static CreateAccountCommand withCurrency(final String currencyCode) {
        return new CreateAccountCommand(Optional.of(AccountStub.fullName), AccountStub.email, AccountStub.username, AccountStub.address, VALID_PASSWORD, currencyCode, AccountStub.phoneNumbers, AccountStub.birthdate, AccountStub.personalId);
    }
}
