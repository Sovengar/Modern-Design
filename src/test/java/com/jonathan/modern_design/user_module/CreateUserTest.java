package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.__config.PrettyTestNames;
import com.jonathan.modern_design.user_module.user.domain.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithInvalidEmail;
import static com.jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithShortPassword;
import static com.jonathan.modern_design._fake_data.UserStub.CreateInvalidUser.createUserWithTooLongPassword;
import static com.jonathan.modern_design._fake_data.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(PrettyTestNames.class)
class CreateUserTest {
    private final UserFacade userFacade = new UserConfiguration().userFacade();

    @Nested
    class ValidUser {
        @Test
        void should_register_user() {
            var data = createUserCommandWithValidData();
            userFacade.registerUser(data);
            var user = userFacade.findUser(new User.ID(data.uuid()));
            assertThat(user).isNotNull();
        }
    }

    @Nested
    class InvalidUser {
        @Test
        void should_fail_when_password_is_too_short() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithShortPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void should_fail_when_password_is_too_long() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithTooLongPassword()))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void should_fail_when_email_is_invalid() {
            assertThatThrownBy(() -> userFacade.registerUser(createUserWithInvalidEmail()))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
