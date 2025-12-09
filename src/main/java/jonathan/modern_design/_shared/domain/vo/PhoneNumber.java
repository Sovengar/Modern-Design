package jonathan.modern_design._shared.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jonathan.modern_design._shared.tags.models.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@ValueObject
@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PACKAGE) //Use factory method
public class PhoneNumber {
    public static final String SPAIN_COUNTRY_CODE = "+34";
    public static final int SPAIN_COUNTRY_CODE_LENGTH = 3;
    private static final Pattern SPANISH_PHONE_PATTERN = Pattern.compile("^(\\+34)?[6789]\\d{8}$");

    @Getter(AccessLevel.NONE)
    String phoneNumber;

    @Transient
    String countryCode;

    public static PhoneNumber of(String telefonMobil) {
        if (!StringUtils.hasText(telefonMobil) || !SPANISH_PHONE_PATTERN.matcher(telefonMobil.replaceAll("\\s+", "")).matches()) {
            throw new IllegalArgumentException("telefonMobil is an invalid phone number for Spain.");
        }

        String normalized = telefonMobil.replaceAll("\\s+", "");
        return new PhoneNumber(
                normalized.startsWith(SPAIN_COUNTRY_CODE)
                        ? normalized.substring(SPAIN_COUNTRY_CODE_LENGTH)
                        : normalized,
                SPAIN_COUNTRY_CODE
        );
    }

    public String getFullNumber() {
        return countryCode + phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber.startsWith(SPAIN_COUNTRY_CODE)
                ? phoneNumber.substring(SPAIN_COUNTRY_CODE_LENGTH)
                : phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final PhoneNumber that)) return false;
        return Objects.equals(getFullNumber(), that.getFullNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullNumber());
    }

    @Override
    public String toString() {
        return getFullNumber();
    }
}
