package jonathan.modern_design.auth;

import jonathan.modern_design.__config.shared_for_all_classes.UnitTest;
import jonathan.modern_design.auth.api.AuthApi;
import jonathan.modern_design.auth.domain.models.User;
import jonathan.modern_design.auth.infra.UsersConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jonathan.modern_design._dsl.UserStub.CreateInvalidUser.createUserWithInvalidEmail;
import static jonathan.modern_design._dsl.UserStub.CreateInvalidUser.createUserWithShortPassword;
import static jonathan.modern_design._dsl.UserStub.CreateInvalidUser.createUserWithTooLongPassword;
import static jonathan.modern_design._dsl.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class CreateUserTest {
    private final AuthApi authApi = new UsersConfig().userApi();

    @Nested
    class WithValidUserShould {
        @Test
        void register_user() {
            var data = createUserCommandWithValidData();
            authApi.registerUser(data);
            var user = authApi.findUser(User.Id.of(data.id()));
            assertThat(user).isNotNull();
        }
    }

    @Nested
    class WithInvalidUserShouldFailIf {
        @Test
        void password_is_too_short() {
            assertThatThrownBy(() -> authApi.registerUser(createUserWithShortPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void password_is_too_long() {
            assertThatThrownBy(() -> authApi.registerUser(createUserWithTooLongPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void email_is_invalid() {
            assertThatThrownBy(() -> authApi.registerUser(createUserWithInvalidEmail()))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
