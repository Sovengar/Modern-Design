package com.jonathan.modern_design.user_module.infra;

import org.mapstruct.factory.Mappers;

public class UserMapperAdapter implements UserMapper {
    private final UserMapper mapStructInstance = Mappers.getMapper(UserMapper.class);
}
