package jonathan.modern_design.auth;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.auth.api.UserApi;
import jonathan.modern_design.auth.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static jonathan.modern_design._fake_data.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRepoIT extends ITConfig {

    @Autowired
    private UserApi userApi;

    @Test
    void should_register_user() {
        var data = createUserCommandWithValidData();
        userApi.registerUser(data);
        var user = userApi.findUser(User.Id.of(data.id()));
        assertThat(user).isNotNull();
    }
}
