package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.__config.RepositoryITConfig;
import com.jonathan.modern_design.user_module.user.domain.model.User.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.jonathan.modern_design._fake_data.UserStub.CreateValidUser.createUserCommandWithValidData;
import static org.assertj.core.api.Assertions.assertThat;

@Import(UserConfiguration.class)
class CreateUserRepoIT extends RepositoryITConfig {

    @Autowired
    private UserFacade userFacade;

    @Test
    void should_register_user() {
        var data = createUserCommandWithValidData();
        userFacade.registerUser(data);
        var user = userFacade.findUser(new UserId(data.uuid()));
        assertThat(user).isNotNull();
    }
}
