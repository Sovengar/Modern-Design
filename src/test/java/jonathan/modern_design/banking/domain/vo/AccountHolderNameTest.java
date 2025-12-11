package jonathan.modern_design.banking.domain.vo;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountHolderNameTest {

    @Test
    void of_givenValidName_returnsAccountHolderName() {
        Optional<String> validName = Optional.of("John Doe");
        AccountHolderName accountHolderName = AccountHolderName.of(validName);

        assertNotNull(accountHolderName);
        assertEquals("John Doe", accountHolderName.getOptionalName().orElse(null));
    }

    @Test
    void of_givenEmptyOptional_returnsAccountHolderNameWithNull() {
        Optional<String> emptyName = Optional.empty();
        AccountHolderName accountHolderName = AccountHolderName.of(emptyName);

        assertNotNull(accountHolderName);
        assertNull(accountHolderName.getOptionalName().orElse(null));
    }

    @Test
    void of_givenNameWithNumbers_throwsUserRealNameNotValidException() {
        Optional<String> invalidName = Optional.of("John123");

        assertThrows(AccountHolderName.UserRealNameNotValidException.class, () ->
                AccountHolderName.of(invalidName));
    }
}
