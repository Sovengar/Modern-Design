package jonathan.modern_design.account_module.domain.vo;

public record AccountAddress(
        String street,
        String city,
        String state,
        String zipCode) {

    public static AccountAddress of(String street, String city, String state, String zipCode) {
        return new AccountAddress(street, city, state, zipCode);
    }

    public static AccountAddress of(String address) {
        return new AccountAddress(address, "", "", "");
    }
}
