package jonathan.modern_design.banking.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jonathan.modern_design._shared.tags.models.ValueObject;
import jonathan.modern_design._shared.tags.persistence.InMemoryOnlyCatalog;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@ValueObject
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PACKAGE) //Use factory method
public class PersonalId {
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-HJ-NP-TV-Z]$");
    private static final Pattern NIE_PATTERN = Pattern.compile("^[XYZ][0-9]{7}[A-Z]$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{5,15}$");
    String value;

    @Enumerated(EnumType.STRING)
    @InMemoryOnlyCatalog
    PersonalIdType type;

    public static PersonalId of(String raw) {
        Objects.requireNonNull(raw, "PersonalId cannot be null");
        String normalized = raw.trim().toUpperCase();

        if (DNI_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, PersonalIdType.DNI);
        }

        if (NIE_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, PersonalIdType.NIE);
        }

        if (PASSPORT_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, PersonalIdType.PASSPORT);
        }

        throw new IllegalArgumentException("Invalid personal ID format: " + raw);
    }

    public enum PersonalIdType {
        DNI, NIE, PASSPORT
    }
}
