package jonathan.modern_design.notification;

import jonathan.modern_design._shared.tags.ApplicationService;
import jonathan.modern_design.banking.api.events.AccountHolderRegistered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class Notifier {
    private final EmailSender emailSender;

    @ApplicationModuleListener
    void sendWelcomeEmail(AccountHolderRegistered event) {
        //TODO
        final var from = "Welcome!";
        final var to = "";
        final var username = "";

        final var subject = "Dear %s, welcome! Sincerely, %s".formatted(username, getCEOName());
        var email = new Email(to, getCcs(), from, subject, "TODO");

        emailSender.sendEmail(email);
    }

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

