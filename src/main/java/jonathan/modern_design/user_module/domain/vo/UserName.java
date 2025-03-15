package jonathan.modern_design.user_module.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class UserName {
    private String username;

    public static UserName of(String name) {
        return new UserName(name);
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserName nameVO)) return false;
        return username.equals(nameVO.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

}
