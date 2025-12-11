package jonathan.modern_design.auth.domain;

import com.github.javafaker.Faker;
import jonathan.modern_design._shared.domain.vo.Email;
import jonathan.modern_design.auth.domain.catalogs.Roles;
import jonathan.modern_design.auth.domain.models.Role;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.domain.vo.UserName;
import jonathan.modern_design.auth.domain.vo.UserPassword;

import java.util.UUID;

public class UserDsl {
    public static final Faker faker = new Faker();
    public static final UUID DEFAULT_ID = UUID.fromString("47611f29-731c-4dcc-966b-3537c35e8ace");
    public static final String VALID_PASSWORD = faker.internet().password(4, 12, true, true, true) + "1ºAa";

    public static User givenATechnician() {
        return User.Factory.register(
                User.Id.of(DEFAULT_ID),
                UserName.of(faker.name().username()),
                Email.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD),
                Role.of(Roles.TECHNICIAN)
        );
    }

    public static User givenAnAdmin() {
        return User.Factory.registerAdmin(
                User.Id.of(DEFAULT_ID),
                UserName.of(faker.name().username()),
                Email.of(faker.internet().emailAddress()),
                Email.of(faker.internet().emailAddress()),
                UserPassword.of(VALID_PASSWORD)
        );
    }
}
