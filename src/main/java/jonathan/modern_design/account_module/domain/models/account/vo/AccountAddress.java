package jonathan.modern_design.account_module.domain.models.account.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value //No record for Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) //For Hibernate
public class AccountAddress {
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
