package jonathan.modern_design.user.api.dtos;

import jonathan.modern_design.user.domain.Role;
import jonathan.modern_design.user.domain.User;

import java.util.Map;

public record UserDto(String realname, String email, String country, String username, Map<String, String> deprecations) {
    public UserDto(User user) {
        this(
                user.realname().getRealname().orElse(""),
                user.email().email(),
                user.country(),
                user.username().username(),
                Map.of("inventedProperty", user.username().username())
        );
    }

    //This Dto is akward because is a 1:1 map with the model and there is nothing to hide
    public record RoleDto(String code, String description) {
        public RoleDto(Role role) {
            this(role.code().roleCode(), role.description());
        }
    }
}
