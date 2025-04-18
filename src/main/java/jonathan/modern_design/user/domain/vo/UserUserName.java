package jonathan.modern_design.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value //No record for Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC) //For Hibernate
public class UserUserName {
    String username;

    public static UserUserName of(final String username) {
        return new UserUserName(username);
    }
}
