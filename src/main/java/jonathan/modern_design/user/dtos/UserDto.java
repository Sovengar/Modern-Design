package jonathan.modern_design.user.dtos;

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
}
