package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.dtos.UserEntity;
import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
interface UserMapper {

    @Mapping(source = "realname", target = "realname", qualifiedByName = "mapRealName")
    @Mapping(source = "username", target = "username", qualifiedByName = "mapUsername")
    @Mapping(source = "email", target = "email", qualifiedByName = "mapEmail")
    @Mapping(source = "password", target = "password", qualifiedByName = "mapPassword")
    User toUser(final UserEntity userEntity);

    Iterable<User> toUsers(Iterable<UserEntity> userEntity);

    @InheritInverseConfiguration
    @Mapping(source = "realname", target = "realname", qualifiedByName = "mapRealName")
    @Mapping(source = "username", target = "username", qualifiedByName = "mapUsername")
    @Mapping(source = "email", target = "email", qualifiedByName = "mapEmail")
    @Mapping(source = "password", target = "password", qualifiedByName = "mapPassword")
    UserEntity toUserEntity(final User user);

    @Named("mapEmail")
    default UserEmailVO mapEmail(String email) {
        return UserEmailVO.of(email);
    }

    @Named("mapPassword")
    default UserPasswordVO mapPassword(String password) {
        return UserPasswordVO.of(password);
    }

    @Named("mapRealName")
    default UserRealNameVO mapRealName(String realname) {
        return UserRealNameVO.of(realname);
    }

    @Named("mapUsername")
    default UserNameVO mapUsername(String username) {
        return UserNameVO.of(username);
    }

    @Named("mapEmail")
    default String mapEmail(UserEmailVO email) {
        return email.getEmail();
    }

    @Named("mapPassword")
    default String mapPassword(UserPasswordVO password) {
        return password.getPassword();
    }

    @Named("mapRealName")
    default String mapRealName(UserRealNameVO realname) {
        return realname.getName();
    }

    @Named("mapUsername")
    default String mapUsername(UserNameVO username) {
        return username.getName();
    }

}
