package jonathan.modern_design.banking.application.create_account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateAccountRequest(
        String fullName,
        @NotEmpty(message = "Email is required") String email,
        @NotEmpty(message = "Username is required") String username,
        @NotNull(message = "Address is required") CreateAccount.Command.Address address,
        @NotEmpty(message = "Password is required") String password,
        @NotNull(message = "Currency is required") String currency,
        @NotNull(message = "PhoneNumbers is required") List<String> phoneNumbers,
        @NotNull(message = "Birthdate is required") LocalDate birthdate,
        @NotEmpty(message = "Personal Id is required") String personalId
) {
}
