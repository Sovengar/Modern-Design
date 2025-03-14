package com.jonathan.modern_design.user_module.user.infra;

import com.jonathan.modern_design._infra.config.annotations.Inyectable;
import org.mapstruct.factory.Mappers;

@Inyectable
public class UserMapperAdapter implements UserMapper {
    private final UserMapper mapStructInstance = Mappers.getMapper(UserMapper.class);
}
