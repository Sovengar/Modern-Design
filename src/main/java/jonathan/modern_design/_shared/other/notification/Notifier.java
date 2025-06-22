package jonathan.modern_design._shared.other.notification;

import jonathan.modern_design._shared.domain.tags.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class Notifier {
    private final EmailSender emailSender;

    //TODO
//    public void sendWelcomeEmail(User user) {
//        emailSender.sendEmail(getEmail(user));
//    }
//
//    private Email getEmail(final User user) {
//        final var from = "Welcome!";
//        final var subject = "Dear %s, welcome! Sincerely, %s".formatted(user.getUsername().getUsername(), getCEOName());
//        final var email = user.getEmail().getEmail();
//        return new Email(email, getCcs(), from, subject, "TODO");
//    }

    private String getCEOName() {
        // Find the CEO in an external system
        return "Jonathan";
    }

    private List<String> getCcs() {
        // Find the contact in an external system
        //String contact = fullName + " <" + ldapUserDto.getWorkEmail().toLowerCase() + ">";
        //email.getCc().add(contact);
        return List.of();
    }
}

