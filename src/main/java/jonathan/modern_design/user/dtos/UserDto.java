package jonathan.modern_design.user.dtos;

import java.util.Map;

public record UserDto(String realname, String email, String country, String username, Map<String, String> deprecations) {
}
