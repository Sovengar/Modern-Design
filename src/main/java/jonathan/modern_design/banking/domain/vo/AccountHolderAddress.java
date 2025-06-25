package jonathan.modern_design.banking.domain.vo;

import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

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

    public static AccountHolderAddress of(String street, String city, String state, String zipCode) {
        return new AccountHolderAddress(street, city, state, zipCode);
    }

    public static AccountHolderAddress of(String address) {
        return new AccountHolderAddress(address, "", "", "");
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + ", " + postalCode;
        //return String.format("Street: %s, City: %s, State: %s, ZipCode: %s", street, city, state, zipCode);
    }
}
