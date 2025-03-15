package jonathan.modern_design.user_module.infra;

import jonathan.modern_design.user_module.UserApi;
import jonathan.modern_design.user_module.application.UserFacade;
import jonathan.modern_design.user_module.application.UserRegister;
import jonathan.modern_design.user_module.domain.RoleRepo;
import jonathan.modern_design.user_module.domain.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class UsersConfig {
    final UserRepo userRepo = new UserInMemoryRepo();

    public UserApi userApi(UserRepo userRepo, RoleRepo roleRepo) {
        UserRegister userRegister = new UserRegister(userRepo, roleRepo);
        return new UserFacade(userRepo, userRegister);
    }

    @Profile("test")
    public UserApi userApi() {
        //For Unit testing
        RoleRepo roleRepo = new RoleRepoInMemory();
        return userApi(userRepo, roleRepo);
    }

    @Profile("test")
    public UserRepo getUserRepo() {
        //For Unit testing
        return userRepo;
    }
}
