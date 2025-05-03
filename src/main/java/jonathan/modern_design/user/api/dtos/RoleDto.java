package jonathan.modern_design.user.api.dtos;

import jonathan.modern_design.user.domain.models.Role;

//If we only do a 1:1 map, we can use the model directly, sadly, we do a slight transformation
//If we use the model directly, the moment we need to diverge, we would need to use a DTO
public record RoleDto(String code, String description) {
    public RoleDto(Role role) {
        this(role.getCode().getRoleCode(), role.getDescription());
    }
}
