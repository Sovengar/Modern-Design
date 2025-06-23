package jonathan.modern_design.notification;

import jonathan.modern_design._shared.tags.ApplicationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
class EmailSMTPSender implements EmailSender {
    @Override
    public void sendEmail(Email email) {
        // Imagine 20 lines of infra code to:
        // - get a SMTP connection to RELAY-COSMO-SMTP server
        // - basic authentication with the mail server
        // - track emails sent and avoid spamming users
        // - more
        log.info("Sending email: {}", email);
    }
}
