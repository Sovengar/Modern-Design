package jonathan.modern_design.user.infra;

import jonathan.modern_design._common.annotations.Inyectable;
import jonathan.modern_design.user.domain.User;
import jonathan.modern_design.user.domain.vo.UserEmail;
import jonathan.modern_design.user.domain.vo.UserName;
import jonathan.modern_design.user.domain.vo.UserPassword;
import jonathan.modern_design.user.domain.vo.UserRealName;
import jonathan.modern_design.user.dtos.UserResource;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

interface UserMapper {
    UserResource toResource(final User user);

    User toDomain(final UserResource userResource);
}

@Mapper(componentModel = "spring")
interface UserMapperStruct {
    Map<String, String> DEPRECATIONS = Map.of("name", "realname");

    @Mapping(target = "deprecations", expression = "java(DEPRECATIONS)")
    UserResource toResource(final User user);

    User toDomain(final UserResource userResource);

    default String mapPassword(final UserPassword password) {
        return password.getPassword();
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

    default String mapUsername(final UserName username) {
        return username.getUsername();
    }

    default UserName mapUsername(final String username) {
        return UserName.of(username);
    }

    default String mapEmail(final UserEmail email) {
        return email.getEmail();
    }

    default UserEmail mapEmail(final String email) {
        return UserEmail.of(email);
    }
}

@Inyectable
@RequiredArgsConstructor
class UserMapperAdapter implements UserMapper {
    private final UserMapperStruct mapStructInstance; //Mappers.getMapper(UserMapperStruct.class)

    @Override
    public UserResource toResource(final User user) {
        return mapStructInstance.toResource(user);
    }

    @Override
    public User toDomain(final UserResource userResource) {
        return mapStructInstance.toDomain(userResource);
    }
}
