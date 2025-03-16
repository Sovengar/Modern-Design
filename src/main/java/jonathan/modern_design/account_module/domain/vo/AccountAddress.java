package jonathan.modern_design.account_module.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAddress {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;

    public static AccountAddress of(String street, String city, String state, String zipCode) {
        return new AccountAddress(street, city, state, zipCode);
    }

    public static AccountAddress of(String address) {
        return new AccountAddress(address, "", "", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAddress accountAddress)) return false;
        return Objects.equals(street, accountAddress.street) &&
                Objects.equals(city, accountAddress.city) &&
                Objects.equals(state, accountAddress.state) &&
                Objects.equals(zipCode, accountAddress.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode);
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }
}
