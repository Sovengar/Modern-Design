package jonathan.modern_design._shared.domain.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.tags.models.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@ValueObject
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PACKAGE) //Use factory method
public class URLStringed {
    String value;

    public static URLStringed of(String value) {
        requireNonNull(value, "value must not be null");

        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("value must not be empty");
        }

        if (!isValidUrl(value)) {
            throw new IllegalArgumentException("The provided string is not valid Url");
        }

        return new URLStringed(value.trim());
    }

    private static boolean isValidUrl(String url) {
        return url.matches("^https?://.*$");
    }
}

