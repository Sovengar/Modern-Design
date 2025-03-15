package jonathan.modern_design.user_module;

import jonathan.modern_design.user_module.domain.User;
import jonathan.modern_design.user_module.dtos.UserRegisterCommand;
import jonathan.modern_design.user_module.dtos.UserResource;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserResource findUser(User.UserId userId);
}
