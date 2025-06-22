package jonathan.modern_design.auth.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.domain.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(staticName = "of")
public class UserName implements ValueObject {
    String username;
}
