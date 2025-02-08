package com.jonathan.modern_design.user_module;

import com.jonathan.modern_design.user_module.dtos.UserEntity;
import org.mapstruct.factory.Mappers;

class UserMapperAdapter implements UserMapper {
    private final UserMapper mapStructInstance = Mappers.getMapper(UserMapper.class);

    @Override
    public User toUser(final UserEntity userEntity) {
        return mapStructInstance.toUser(userEntity);
    }

    @Override
    public Iterable<User> toUsers(Iterable<UserEntity> userEntity) {
        return mapStructInstance.toUsers(userEntity);
    }

    @Override
    public UserEntity toUserEntity(final User user) {
        return mapStructInstance.toUserEntity(user);
    }

}
