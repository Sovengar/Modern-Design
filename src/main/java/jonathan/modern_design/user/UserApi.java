package jonathan.modern_design.user;

import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.dtos.UserDto;
import jonathan.modern_design.user.dtos.UserRegisterCommand;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserDto findUser(User.UserId userId);
}
