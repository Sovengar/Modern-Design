package jonathan.modern_design.banking.domain.vo;

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
        List<String> phoneNumbers = List.of("1234567890");
        AccountHolderPhoneNumbers accountHolderPhoneNumbers = AccountHolderPhoneNumbers.of(phoneNumbers);

        assertNotNull(accountHolderPhoneNumbers);
        assertEquals(Set.of("1234567890"), accountHolderPhoneNumbers.getPhoneNumbers());
    }

    @Test
    void testOf_ValidMultiplePhoneNumbers() {
        List<String> phoneNumbers = List.of("1234567890", "0987654321");
        AccountHolderPhoneNumbers accountHolderPhoneNumbers = AccountHolderPhoneNumbers.of(phoneNumbers);

        assertNotNull(accountHolderPhoneNumbers);
        assertEquals(Set.of("1234567890", "0987654321"), accountHolderPhoneNumbers.getPhoneNumbers());
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
        assertThrows(AccountHolderPhoneNumbers.InvalidPhoneNumbersException.class, () -> AccountHolderPhoneNumbers.of(phoneNumbers));
    }

    @Test
    void testOf_DuplicatePhoneNumbers() {
        List<String> phoneNumbers = List.of("1234567890", "1234567890");
        assertThrows(IllegalArgumentException.class, () -> AccountHolderPhoneNumbers.of(phoneNumbers));
    }
}
