package jonathan.modern_design.auth.api.dtos;

import jonathan.modern_design.auth.domain.models.User;

import java.util.Map;

public record UserDto(String email, String username, Map<String, String> deprecations, Boolean isCreated) {
    public UserDto(User user, Boolean isCreated) {
        this(
                user.getEmail().getEmail(),
                user.getUsername().getUsername(),
                Map.of("inventedProperty", user.getUsername().getUsername()),
                isCreated //Exposing the problems of mapping inside the Dto
        );
    }
}
