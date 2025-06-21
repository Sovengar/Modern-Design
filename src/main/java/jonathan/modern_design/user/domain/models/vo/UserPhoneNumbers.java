package jonathan.modern_design.user.domain.models.vo;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jonathan.modern_design._shared.config.exception.RootException;
import jonathan.modern_design._shared.tags.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.E164;
import static lombok.AccessLevel.PRIVATE;

@Embeddable
@Data //No record for Hibernate
@NoArgsConstructor(access = PRIVATE, force = true) //For Hibernate
@AllArgsConstructor(access = PRIVATE)
public class UserPhoneNumbers implements ValueObject {
    private static final String SEPARATOR = ";";
    private static final String PHONE_NUMBER_REGEX = "^(?:\\+?\\d{1,4}[\\s.-]?)?(?:\\(?\\d+\\)?[\\s.-]?)*\\d+(?:\\s?(?:x|ext\\.?)\\s?\\d{1,5})?$\n";

    /**
     * Double Dispatch, delegating from VO to a third party library.
     * At this point, while we keep acknowledging the degree of coupling with the third party library (keeping it to the bare minimum),
     * there is no problem.
     */
    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    @Transient
    Set<String> phoneNumbersSet = new HashSet<>();
    String phoneNumbers;

    private UserPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbersSet = transformStringToSet(transformListToString(phoneNumbers));
        this.phoneNumbers = transformSetToString();
    }

    public static UserPhoneNumbers of(List<String> phoneNumbers) {
        UserPhoneNumbers userPhoneNumbers = new UserPhoneNumbers(phoneNumbers);
        validatePhoneNumbers(userPhoneNumbers.getPhoneNumbers());

        return userPhoneNumbers;
    }

    private static Set<String> transformStringToSet(String phoneNumbers) {
        return Set.of(phoneNumbers.split(SEPARATOR));
    }

    private static void validatePhoneNumbers(Set<String> phoneNumbersSet) {
        phoneNumbersSet.forEach(userPhoneNumber -> {
//            if (!userPhoneNumber.matches(PHONE_NUMBER_REGEX)) {
//                throw new InvalidPhoneNumbersException();
//            }
        });
    }

    private static String validateAndNormalizePhoneNumber(String value) {
        try {
            if (Long.parseLong(value) <= 0) {
                throw new InvalidPhoneNumbersException("The phone number cannot be negative: " + value);
            }
            final var phoneNumber = PHONE_NUMBER_UTIL.parse(value, "ES");
            final String formattedPhoneNumber = PHONE_NUMBER_UTIL.format(phoneNumber, E164);
            // E164 format returns a phone number with + character
            return formattedPhoneNumber.substring(1);
        } catch (NumberParseException | NumberFormatException e) {
            throw new InvalidPhoneNumbersException("The phone number isn't valid: " + value, e);
        }
    }

    private String transformListToString(List<String> phoneNumbers) {
        return String.join(SEPARATOR, phoneNumbers);
    }

    //If this logic grows, for example, based on the role allowing more phones, move to a domain Service
    private boolean hasMoreThanTwoPhoneNumbers() {
        return phoneNumbersSet.size() > 2;
    }

    private String transformSetToString() {
        return String.join(SEPARATOR, phoneNumbersSet);
    }

    public void addPhoneNumber(String phoneNumber) {
        phoneNumbersSet.add(phoneNumber);
        if (hasMoreThanTwoPhoneNumbers()) {
            throw new MaximumNumberOfPhoneNumbersExceededException();
        }
        phoneNumbers = transformSetToString();
    }

    public void removePhoneNumber(String phoneNumber) {
        phoneNumbersSet.remove(phoneNumber);
        phoneNumbers = transformSetToString();
    }

    public boolean containsPhoneNumber(String phoneNumber) {
        return phoneNumbersSet.contains(phoneNumber);
    }

    public boolean isEmpty() {
        return phoneNumbersSet.isEmpty();
    }

    public Set<String> getPhoneNumbers() {
        return Collections.unmodifiableSet(phoneNumbersSet);
    }

    public Optional<String> getConcreteEmail(String phoneNumber) {
        return phoneNumbersSet.stream().filter(phone -> phone.equals(phoneNumber)).findFirst();
    }

    private static class MaximumNumberOfPhoneNumbersExceededException extends RootException {
        @Serial private static final long serialVersionUID = -6507485328113799878L;

        MaximumNumberOfPhoneNumbersExceededException() {
            super("Maximum number of phone numbers exceeded");
        }
    }

    static class InvalidPhoneNumbersException extends RootException {
        @Serial private static final long serialVersionUID = -6507485328113799878L;

        InvalidPhoneNumbersException(String msg) {
            super(msg);
        }

        InvalidPhoneNumbersException(String msg, Throwable cause) {
            super(HttpStatus.INTERNAL_SERVER_ERROR, msg, "", cause);
        }

        InvalidPhoneNumbersException() {
            super("Invalid phone numbers");
        }
    }
}
