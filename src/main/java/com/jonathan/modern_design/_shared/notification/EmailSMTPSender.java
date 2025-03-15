package com.jonathan.modern_design._shared.notification;

import com.jonathan.modern_design._internal.config.annotations.Inyectable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Inyectable
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
