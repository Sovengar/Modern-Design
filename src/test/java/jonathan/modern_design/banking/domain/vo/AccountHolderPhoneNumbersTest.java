package jonathan.modern_design.banking.domain.vo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountHolderPhoneNumbersTest {

    @Test
    void testOf_ValidSinglePhoneNumber() {
        List<String> phoneNumbers = List.of("600000000");
        AccountHolderPhoneNumbers accountHolderPhoneNumbers = AccountHolderPhoneNumbers.of(phoneNumbers);

        assertNotNull(accountHolderPhoneNumbers);
        // Expects E164 without leading + (34 prefix for Spain)
        assertEquals(Set.of("34600000000"), accountHolderPhoneNumbers.getPhoneNumbers());
    }

    @Test
    void testOf_ValidMultiplePhoneNumbers() {
        List<String> phoneNumbers = List.of("600000000", "910000000");
        AccountHolderPhoneNumbers accountHolderPhoneNumbers = AccountHolderPhoneNumbers.of(phoneNumbers);

        assertNotNull(accountHolderPhoneNumbers);
        assertEquals(Set.of("34600000000", "34910000000"), accountHolderPhoneNumbers.getPhoneNumbers());
    }

    @Test
    void testOf_EmptyPhoneNumbersList() {
        List<String> phoneNumbers = List.of();
        AccountHolderPhoneNumbers accountHolderPhoneNumbers = AccountHolderPhoneNumbers.of(phoneNumbers);

        assertNotNull(accountHolderPhoneNumbers);
        assertThat(accountHolderPhoneNumbers.getPhoneNumbers()).isEmpty();
    }

    @Test
    void testOf_InvalidPhoneNumber() {
        List<String> phoneNumbers = List.of("invalid");
        assertThrows(AccountHolderPhoneNumbers.InvalidPhoneNumbersException.class,
                () -> AccountHolderPhoneNumbers.of(phoneNumbers));
    }

    @Test
    void testOf_DuplicatePhoneNumbers() {
        // Duplicates will be normalized to same number
        List<String> phoneNumbers = List.of("600000000", "600000000");
        // Implementation uses Set, so duplicates are just ignored/merged, NOT an error
        // unless explicitly checked?
        // Code: `validateAndNormalizePhoneNumber` -> List -> `new
        // AccountHolderPhoneNumbers(List)` -> `transformListToString` ->
        // `transformStringToSet`.
        // `transformStringToSet` uses `Set.of`. `Set.of` throws
        // IllegalArgumentException if duplicates!
        assertThrows(IllegalArgumentException.class, () -> AccountHolderPhoneNumbers.of(phoneNumbers));
    }
}
