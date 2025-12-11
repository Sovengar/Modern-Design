package jonathan.modern_design.banking.application.create_account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record CreateAccountCommand(
        Optional<String> fullName,
        @NotEmpty(message = "Email is required") String email,
        @NotEmpty(message = "Username is required") String username,
        @NotNull(message = "Address is required") Address address,
        @NotEmpty(message = "Password is required") String password,
        @NotNull(message = "Currency is required") String currency,
        @NotNull(message = "PhoneNumbers is required") List<String> phoneNumbers,
        @NotNull(message = "Birthdate is required") LocalDate birthdate,
        @NotEmpty(message = "Personal Id is required") String personalId
) {

    public record Address(String street, String city, String state, String zipCode, String countryCode) {
    }
}
