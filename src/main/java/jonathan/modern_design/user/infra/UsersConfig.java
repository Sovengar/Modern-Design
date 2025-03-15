package jonathan.modern_design.user.infra;

import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.application.UserFacade;
import jonathan.modern_design.user.application.UserRegister;
import jonathan.modern_design.user.domain.RoleRepo;
import jonathan.modern_design.user.domain.UserRepo;
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
