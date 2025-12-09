package jonathan.modern_design.banking.domain.vo;

import jakarta.persistence.Convert;
import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.tags.models.ValueObject;
import jonathan.modern_design._shared.tags.persistence.InMemoryOnlyCatalog;
import jonathan.modern_design._shared.tags.persistence.LinkedAsFKinDB;
import jonathan.modern_design.banking.infra.store.converters.StreetTypeToCodeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

//@Embeddable
@ValueObject
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE) //Use factory method
public class AccountHolderAddress {
    @Convert(converter = StreetTypeToCodeConverter.class) //We can preserve the enum gaining semantics instead of using a primitive "String"
    //This is useful when we prefer using indexable codes instead of enum strings or in legacy systems where catalogs are already created.
    @InMemoryOnlyCatalog
    StreetType streetType;

    String street;
    String city;
    String state;
    String postalCode;

    @LinkedAsFKinDB
    //TODO CREATE COUNTRY TABLE, AND USE HIS ID HERE, ALSO CREATE THE SCRIPT
    String country;

    public static AccountHolderAddress of(StreetType streetType, String street, String city, String state, String zipCode, Country country) {
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

        return new AccountHolderAddress(streetType, street, city, state, zipCode, country.code());
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street, city, state, postalCode, country);
    }

    @Getter
    @RequiredArgsConstructor
    public enum StreetType {
        STREET("0001", "Street"),
        AVENUE("0002", "Avenue"),
        BOULEVARD("0003", "Boulevard"),
        WAY("0004", "Way"),
        CIRCLE("0005", "Circle"),
        PARK("0006", "Park"),
        TERRACE("0007", "Terrace"),
        SQUARE("0008", "Square"),
        LANE("0009", "Lane"),
        ROAD("0010", "Road"),
        DRIVE("0011", "Drive"),
        WAYS("0012", "Ways"),
        CIRCLE_ROAD("0013", "Circle road"),
        PARKWAY("0014", "Parkway"),
        TERRACE_ROAD("0015", "Terrace road"),
        SQUARE_ROAD("0016", "Square road"),
        LANE_ROAD("0017", "Lane road"),
        ROAD_WAY("0018", "Road way"),
        DRIVE_WAY("0019", "Drive way");
        private final String code;
        private final String description;
    }
}
