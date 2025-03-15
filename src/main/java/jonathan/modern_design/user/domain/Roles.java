package jonathan.modern_design.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    ADMIN("ADM", "Admin"),
    TECHNICIAN("TEC", "Technician"),
    USER("USER", "User");

    private final String code;
    private final String description;

    public static Roles fromCode(String code) {
        for (Roles role : Roles.values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
