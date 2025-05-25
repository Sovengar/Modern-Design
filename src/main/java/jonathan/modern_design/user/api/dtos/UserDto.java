package jonathan.modern_design.user.api.dtos;

import jonathan.modern_design.user.domain.models.User;

import java.util.Map;

public record UserDto(String realname, String email, String country, String username, Map<String, String> deprecations, Boolean isCreated) {
    public UserDto(User user, Boolean isCreated) {
        this(
                user.getRealNameOrPlaceHolder(),
                user.getEmail().getEmail(),
                user.getCountry(),
                user.getUsername().getUsername(),
                Map.of("inventedProperty", user.getUsername().getUsername()),
                isCreated //Exposing the problems of mapping inside the Dto
        );
    }
}
