package jonathan.modern_design.user.domain.models.vo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserPhoneNumbersTest {

    @Test
    void testOf_ValidSinglePhoneNumber() {
        List<String> phoneNumbers = List.of("1234567890");
        UserPhoneNumbers userPhoneNumbers = UserPhoneNumbers.of(phoneNumbers);

        assertNotNull(userPhoneNumbers);
        assertEquals(Set.of("1234567890"), userPhoneNumbers.getPhoneNumbers());
    }

    @Test
    void testOf_ValidMultiplePhoneNumbers() {
        List<String> phoneNumbers = List.of("1234567890", "0987654321");
        UserPhoneNumbers userPhoneNumbers = UserPhoneNumbers.of(phoneNumbers);

        assertNotNull(userPhoneNumbers);
        assertEquals(Set.of("1234567890", "0987654321"), userPhoneNumbers.getPhoneNumbers());
    }

    @Test
    void testOf_EmptyPhoneNumbersList() {
        List<String> phoneNumbers = List.of();
        UserPhoneNumbers userPhoneNumbers = UserPhoneNumbers.of(phoneNumbers);

        assertNotNull(userPhoneNumbers);
        assertTrue(userPhoneNumbers.phoneNumbers().isEmpty());
    }

    @Test
    void testOf_InvalidPhoneNumber() {
        List<String> phoneNumbers = List.of("invalid");
        assertThrows(UserPhoneNumbers.InvalidPhoneNumbersException.class, () -> UserPhoneNumbers.of(phoneNumbers));
    }

    @Test
    void testOf_DuplicatePhoneNumbers() {
        List<String> phoneNumbers = List.of("1234567890", "1234567890");
        assertThrows(IllegalArgumentException.class, () -> UserPhoneNumbers.of(phoneNumbers));
    }
}
