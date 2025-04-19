package jonathan.modern_design.user.application;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.UserApi;
import jonathan.modern_design.user.domain.User.UserId;
import jonathan.modern_design.user.dtos.UserDto;
import jonathan.modern_design.user.dtos.UserRegisterCommand;
import lombok.RequiredArgsConstructor;

@Injectable
@RequiredArgsConstructor
public class UserFacade implements UserApi {
    private final RegisterUser registerUser;
    private final UserFinder userFinder;

    @Override
    public void registerUser(UserRegisterCommand command) {
        registerUser.handle(command);
    }

    @Override
    public UserDto findUser(UserId userId) {
        return userFinder.queryWith(userId);
    }
}
