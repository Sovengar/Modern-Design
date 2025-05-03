package jonathan.modern_design.user.domain.models.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._common.annotations.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(staticName = "of")
public class UserUserName implements ValueObject {
    String username;
}
