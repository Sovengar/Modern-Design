package jonathan.modern_design.user.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserName(String username) {
    public static UserName of(String name) {
        return new UserName(name);
    }
}
