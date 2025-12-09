package jonathan.modern_design.auth.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.tags.models.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@ValueObject
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(staticName = "of") //Use factory method
public class UserName {
    String username;
}
