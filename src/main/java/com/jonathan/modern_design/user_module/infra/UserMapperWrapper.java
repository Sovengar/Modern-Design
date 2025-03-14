package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design._infra.config.annotations.Inyectable;
import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.dtos.UserResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

interface UserMapper {
    UserResource toResource(final User user);

    User toDomain(final UserResource userResource);
}

@Mapper
interface UserMapperStruct {
}
    
@Inyectable
class UserMapperAdapter implements UserMapper {
    private final UserMapper mapStructInstance = Mappers.getMapper(UserMapper.class);

    @Override
    public UserResource toResource(final User user) {
        return mapStructInstance.toResource(user);
    }

    @Override
    public User toDomain(final UserResource userResource) {
        return mapStructInstance.toDomain(userResource);
    }
}
