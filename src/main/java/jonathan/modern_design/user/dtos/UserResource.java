package jonathan.modern_design.user.dtos;

import java.util.Map;

public record UserResource(String realname, String email, String country, String username, Map<String, String> deprecations) {
}
