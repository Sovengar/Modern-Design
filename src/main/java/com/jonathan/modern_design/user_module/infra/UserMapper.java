package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.model.UserEmail;
import com.jonathan.modern_design.user_module.domain.model.UserName;
import com.jonathan.modern_design.user_module.domain.model.UserPassword;
import com.jonathan.modern_design.user_module.domain.model.UserRealName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface UserMapper {

    @Mapping(source = "realname", target = "realname", qualifiedByName = "mapRealName")
    @Mapping(source = "username", target = "username", qualifiedByName = "mapUsername")
    @Mapping(source = "email", target = "email", qualifiedByName = "mapEmail")
    @Mapping(source = "password", target = "password", qualifiedByName = "mapPassword")
    User toUser(final UserEntity userEntity);

    Iterable<User> toUsers(Iterable<UserEntity> userEntity);

    @Mapping(source = "realname", target = "realname", qualifiedByName = "mapRealName")
    @Mapping(source = "username", target = "username", qualifiedByName = "mapUsername")
    @Mapping(source = "email", target = "email", qualifiedByName = "mapEmail")
    @Mapping(source = "password", target = "password", qualifiedByName = "mapPassword")
    UserEntity toUserEntity(final User user);

    @Named("mapEmail")
    default UserEmail mapEmail(String email) {
        return UserEmail.of(email);
    }

    @Named("mapPassword")
    default UserPassword mapPassword(String password) {
        return UserPassword.of(password);
    }

    @Named("mapRealName")
    default UserRealName mapRealName(String realname) {
        return UserRealName.of(realname);
    }

    @Named("mapUsername")
    default UserName mapUsername(String username) {
        return UserName.of(username);
    }

    @Named("mapEmail")
    default String mapEmail(UserEmail email) {
        return email.getEmail();
    }

    @Named("mapPassword")
    default String mapPassword(UserPassword password) {
        return password.getPassword();
    }

    @Named("mapRealName")
    default String mapRealName(UserRealName realname) {
        return realname.getName();
    }

    @Named("mapUsername")
    default String mapUsername(UserName username) {
        return username.getName();
    }

}
