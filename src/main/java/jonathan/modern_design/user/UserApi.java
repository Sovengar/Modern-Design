package jonathan.modern_design.user;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.application.UserFinder;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.dtos.UserDto;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

public interface UserApi {
    void registerUser(UserRegisterCommand command);

    UserDto findUser(User.UserId userId);

    @Injectable
    @RequiredArgsConstructor
    class UserInternalApi implements UserApi {
        private final RegisterUser registerUser;
        private final UserFinder userFinder;

        @Override
        public void registerUser(UserRegisterCommand command) {
            registerUser.handle(command);
        }

        @Override
        public UserDto findUser(User.UserId userId) {
            return userFinder.queryWith(userId);
        }
    }
}
