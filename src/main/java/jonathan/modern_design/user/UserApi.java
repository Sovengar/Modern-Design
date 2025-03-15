package jonathan.modern_design.user;

import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import jonathan.modern_design.user.dtos.UserResource;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserResource findUser(User.UserId userId);
}
