package jonathan.modern_design.banking.domain.models.account.vo;

import jakarta.persistence.Embeddable;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Value //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class AccountAddress implements ValueObject {
    String street;
    String city;
    String state;
    String zipCode;

    public static AccountAddress of(String street, String city, String state, String zipCode) {
        return new AccountAddress(street, city, state, zipCode);
    }

    public static AccountAddress of(String address) {
        return new AccountAddress(address, "", "", "");
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + ", " + zipCode;
        //return String.format("Street: %s, City: %s, State: %s, ZipCode: %s", street, city, state, zipCode);
    }
}
