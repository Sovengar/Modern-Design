package com.jonathan.modern_design._shared.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
