package jonathan.modern_design.auth.application;

import jonathan.modern_design.auth.domain.UserDsl;

//It is more an ObjectMother than Dsl, migrate to Dsl if needed.
public class CreateUserDsl {
    public static RegisterUser.Command withValidData() {
        return new RegisterUser.Command(UserDsl.DEFAULT_ID, UserDsl.faker.name().username(), UserDsl.faker.internet().emailAddress(), UserDsl.VALID_PASSWORD);
    }

    public static RegisterUser.Command createUserWithShortPassword() {
        return new RegisterUser.Command(UserDsl.DEFAULT_ID, UserDsl.faker.name().username(), UserDsl.faker.internet().emailAddress(), UserDsl.faker.internet().password(3, 6));
    }

    public static RegisterUser.Command createUserWithTooLongPassword() {
        return new RegisterUser.Command(UserDsl.DEFAULT_ID, UserDsl.faker.name().username(), UserDsl.faker.internet().emailAddress(), UserDsl.faker.internet().password(55, 55));
    }

    public static RegisterUser.Command createUserWithInvalidEmail() {
        return new RegisterUser.Command(UserDsl.DEFAULT_ID, UserDsl.faker.name().username(), "invalid_email", UserDsl.VALID_PASSWORD);
    }
}
