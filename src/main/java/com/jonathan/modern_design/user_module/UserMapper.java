package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.dtos.UserEntity;
import com.jonathan.modern_design.user_module.vo.UserEmailVO;
import com.jonathan.modern_design.user_module.vo.UserNameVO;
import com.jonathan.modern_design.user_module.vo.UserPasswordVO;
import com.jonathan.modern_design.user_module.vo.UserRealNameVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
interface UserMapper {

    User toUser(final UserEntity userEntity);

    Iterable<User> toUsers(Iterable<UserEntity> userEntity);

    @InheritInverseConfiguration
    UserEntity toUserEntity(final User user);

    default UserEmailVO mapEmail(String email) {
        return UserEmailVO.of(email);
    }

    default UserPasswordVO mapPassword(String password) {
        return UserPasswordVO.of(password);
    }

    default UserRealNameVO mapRealName(String realname) {
        return UserRealNameVO.of(realname);
    }

    default UserNameVO mapUsername(String username) {
        return UserNameVO.of(username);
    }

}
