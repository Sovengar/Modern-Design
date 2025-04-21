package jonathan.modern_design.user.alternatives;

import jonathan.modern_design._common.annotations.Injectable;
import jonathan.modern_design.user.api.dtos.UserDto;
import jonathan.modern_design.user.domain.models.User;
import jonathan.modern_design.user.domain.models.vo.UserEmail;
import jonathan.modern_design.user.domain.models.vo.UserPassword;
import jonathan.modern_design.user.domain.models.vo.UserRealName;
import jonathan.modern_design.user.domain.models.vo.UserUserName;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

interface UserMapper {
    UserDto toResource(final User user);

    User toDomain(final UserDto userDto);
}

@Mapper(componentModel = "spring")
interface UserMapperStruct {
    Map<String, String> DEPRECATIONS = Map.of("name", "realname");

    @Mapping(target = "deprecations", expression = "java(DEPRECATIONS)")
    UserDto toResource(final User user);

    User toDomain(final UserDto userDto);

    default String mapPassword(final UserPassword password) {
        return password.password();
    }

    default UserPassword mapPassword(final String password) {
        return UserPassword.of(password);
    }

    default String mapStatus(final User.Status status) {
        return status.name();
    }

    default User.Status mapStatus(final String status) {
        return User.Status.valueOf(status);
    }

    default String mapRealname(final UserRealName realname) {
        return realname.getRealname().orElse("");
    }

    default UserRealName mapRealname(final String realname) {
        return UserRealName.of(realname);
    }

    default String mapUsername(final UserUserName username) {
        return username.username();
    }

    default UserUserName mapUsername(final String username) {
        return UserUserName.of(username);
    }

    default String mapEmail(final UserEmail email) {
        return email.email();
    }

    default UserEmail mapEmail(final String email) {
        return UserEmail.of(email);
    }
}

@Injectable
@RequiredArgsConstructor
class UserMapperAdapter implements UserMapper {
    private final UserMapperStruct mapStructInstance; //Mappers.getMapper(UserMapperStruct.class)

    @Override
    public UserDto toResource(final User user) {
        return mapStructInstance.toResource(user);
    }

    @Override
    public User toDomain(final UserDto userDto) {
        return mapStructInstance.toDomain(userDto);
    }
}
