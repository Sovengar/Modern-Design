package jonathan.modern_design.banking.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class PersonalId {
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-HJ-NP-TV-Z]$");
    private static final Pattern NIE_PATTERN = Pattern.compile("^[XYZ][0-9]{7}[A-Z]$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{5,15}$");
    @Column(name = "personal_id_value", nullable = false, length = 20)
    String value;
    @Enumerated(EnumType.STRING)
    @Column(name = "personal_id_type", nullable = false, length = 10)
    Type type;

    public static PersonalId of(String raw) {
        Objects.requireNonNull(raw, "PersonalId cannot be null");
        String normalized = raw.trim().toUpperCase();

        if (DNI_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, Type.DNI);
        }

        if (NIE_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, Type.NIE);
        }

        if (PASSPORT_PATTERN.matcher(normalized).matches()) {
            return new PersonalId(normalized, Type.PASSPORT);
        }

        throw new IllegalArgumentException("Invalid personal ID format: " + raw);
    }

    public enum Type {
        DNI, NIE, PASSPORT
    }
}
