package jonathan.modern_design._fake_data;

import jonathan.modern_design.__config.Stub;
import jonathan.modern_design._shared.country.Country;
import jonathan.modern_design.auth.application.RegisterUser;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.vo.UserEmail;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;
import jonathan.modern_design.banking.domain.vo.AccountHolderName;
import jonathan.modern_design.banking.domain.vo.AccountHolderPhoneNumbers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserStub extends Stub {
    public static final Country SPAIN = new Country("ES", "Spain");
    public static final String DEFAULT_COUNTRY = "ES";
    public static final UUID DEFAULT_UUID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");

    public static final String VALID_PASSWORD = faker.internet().password(4, 12, true, true, true) + "1ºAa";

    public static User normalUser() {
        return User.Factory.register(
                User.Id.of(DEFAULT_UUID),
                AccountHolderName.of(faker.name().fullName()),
                UserName.of(faker.name().username()),
                UserEmail.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD),
                SPAIN,
                AccountHolderPhoneNumbers.of(List.of(faker.phoneNumber().phoneNumber())),
                Role.of(Roles.TECHNICIAN));
    }

    public static User adminUser() {
        return User.Factory.registerAdmin(
                User.Id.of(DEFAULT_UUID),
                AccountHolderName.of(faker.name().fullName()),
                UserName.of(faker.name().username()),
                UserEmail.of(faker.internet().emailAddress()),
                UserEmail.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD),
                AccountHolderPhoneNumbers.of(List.of(faker.phoneNumber().phoneNumber())),
                SPAIN);
    }

    public static class CreateValidUser extends Stub {
        public static RegisterUser.Command createUserCommandWithValidData() {
            return new RegisterUser.Command(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), VALID_PASSWORD, DEFAULT_COUNTRY, List.of(faker.phoneNumber().phoneNumber()));
        }
    }

    public static class CreateInvalidUser extends Stub {
        public static RegisterUser.Command createUserWithShortPassword() {
            return new RegisterUser.Command(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(3, 6), DEFAULT_COUNTRY, List.of(faker.phoneNumber().phoneNumber()));
        }

        public static RegisterUser.Command createUserWithTooLongPassword() {
            return new RegisterUser.Command(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), faker.internet().emailAddress(), faker.internet().password(55, 55), DEFAULT_COUNTRY, List.of(faker.phoneNumber().phoneNumber()));
        }

        public static RegisterUser.Command createUserWithInvalidEmail() {
            return new RegisterUser.Command(DEFAULT_UUID, Optional.ofNullable(faker.name().fullName()), faker.name().username(), "invalid_email", VALID_PASSWORD, DEFAULT_COUNTRY, List.of(faker.phoneNumber().phoneNumber()));
        }
    }
}
