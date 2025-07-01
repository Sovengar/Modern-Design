package jonathan.modern_design._shared.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class BirthDate implements ValueObject {
    LocalDate birthdate;

    public static BirthDate of(final LocalDate birthdate) {
        Objects.requireNonNull(birthdate, "Birthdate cannot be null");

        var notBorn = LocalDate.now().isBefore(birthdate);

        var aDecadeAgo = LocalDate.now().minusYears(100);
        var eighteenYearsAgo = LocalDate.now().minusYears(18);

        var isTooOld = birthdate.isBefore(aDecadeAgo) || birthdate.isEqual(aDecadeAgo);
        var isTooYoung = birthdate.isAfter(eighteenYearsAgo);

        if (notBorn) {
            throw new InvalidBirthDateException("You can't be born yet");
        } else if (isTooOld) {
            throw new InvalidBirthDateException("Too old");
        } else if (isTooYoung) {
            throw new InvalidBirthDateException("Too young");
        } else {
            return new BirthDate(birthdate);
        }
    }

    static class InvalidBirthDateException extends RuntimeException {
        @Serial private static final long serialVersionUID = 4845268765308658437L;

        InvalidBirthDateException(String message) {
            super(message);
        }
    }
}
