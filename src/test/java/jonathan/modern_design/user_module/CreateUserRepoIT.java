package jonathan.modern_design.user_module;

import jonathan.modern_design.__config.ITConfig;
import jonathan.modern_design.user_module.domain.User.UserId;
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
        var user = userApi.findUser(new UserId(data.uuid()));
        assertThat(user).isNotNull();
    }
}
