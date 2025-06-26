package jonathan.modern_design.banking.domain.vo;

import jonathan.modern_design._shared.domain.Country;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

//@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class AccountHolderAddress implements ValueObject {
    String street;
    String city;
    String state;
    String postalCode;
    String country;

    public static AccountHolderAddress of(String street, String city, String state, String zipCode, Country country) {
        if (!StringUtils.hasText(street)) {
            throw new IllegalArgumentException("Street cannot be null or blank.");
        }

        if (!StringUtils.hasText(city)) {
            throw new IllegalArgumentException("City cannot be null or blank.");
        }

        if (!StringUtils.hasText(state)) {
            throw new IllegalArgumentException("State cannot be null or blank.");
        }

        if (!StringUtils.hasText(zipCode)) {
            throw new IllegalArgumentException("Zip code cannot be null or blank.");
        }

        if (Objects.isNull(country) || !StringUtils.hasText(country.code())) {
            throw new IllegalArgumentException("Country cannot be null or blank");
        }

        return new AccountHolderAddress(street, city, state, zipCode, country.code());
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street, city, state, postalCode, country);
    }
}
