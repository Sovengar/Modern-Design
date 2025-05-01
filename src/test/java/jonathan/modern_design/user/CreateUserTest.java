package jonathan.modern_design.user;

import jonathan.modern_design.__config.PrettyTestNames;
import jonathan.modern_design.user.api.UserApi;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.infra.UsersConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithInvalidEmail;
import static jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithShortPassword;
import static jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithTooLongPassword;
import static jonathan.modern_design._fake_data.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(PrettyTestNames.class)
class CreateUserTest {
    private final UserApi userFacade = new UsersConfig().userApi();

    @Nested
    class WithValidUserShould {
        @Test
        void register_user() {
            var data = createUserCommandWithValidData();
            userFacade.registerUser(data);
            var user = userFacade.findUser(User.Id.of(data.id()));
            assertThat(user).isNotNull();
        }
    }

    @Nested
    class WithInvalidUserShouldFailIf {
        @Test
        void password_is_too_short() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithShortPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void password_is_too_long() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithTooLongPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void email_is_invalid() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithInvalidEmail()))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
