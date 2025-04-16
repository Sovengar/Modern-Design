package jonathan.modern_design.user.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //For Hibernate
@Embeddable
public class UserPhoneNumbers {
    private static final String SEPARATOR = ";";
    private static final String PHONE_NUMBER_REGEX = "^\\+?[1-9]\\d{0,2}[- ]?\\d{3,12}$";
    @Transient
    private Set<String> phoneNumbersSet = new HashSet<>();
    private String phoneNumbers;

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
            if (!userPhoneNumber.matches(PHONE_NUMBER_REGEX)) {
                throw new InvalidPhoneNumbersException();
            }
        });
    }

    private String transformListToString(List<String> phoneNumbers) {
        return String.join(SEPARATOR, phoneNumbers);
    }

    //If this logic grows, for example based on the role allowing more phones, move to a domain Service
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

    private static class MaximumNumberOfPhoneNumbersExceededException extends RuntimeException {
        @Serial private static final long serialVersionUID = -6507485328113799878L;

        MaximumNumberOfPhoneNumbersExceededException() {
            super("Maximum number of phone numbers exceeded");
        }
    }

    private static class InvalidPhoneNumbersException extends RuntimeException {
        @Serial private static final long serialVersionUID = -6507485328113799878L;

        InvalidPhoneNumbersException() {
            super("Invalid phone numbers");
        }
    }
}
