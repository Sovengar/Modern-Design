package jonathan.modern_design.user;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.application.RegisterUser;
import jonathan.modern_design.user.application.UserFinder;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.infra.UserDto;
import lombok.RequiredArgsConstructor;

public interface UserApi {
    void registerUser(RegisterUser.Command command);

    UserDto findUser(User.UserId userId);

    @Injectable
    @RequiredArgsConstructor
    class UserInternalApi implements UserApi {
        private final RegisterUser registerUser;
        private final UserFinder userFinder;

        @Override
        public void registerUser(RegisterUser.Command command) {
            registerUser.handle(command);
        }

        @Override
        public UserDto findUser(User.UserId userId) {
            return userFinder.queryWith(userId);
        }
    }
}
