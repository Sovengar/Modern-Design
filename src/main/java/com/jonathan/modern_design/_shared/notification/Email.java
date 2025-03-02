package com.jonathan.modern_design._shared.notification;

import java.util.List;


public record Email(
        String to,
        List<String> cc,
        String from,
        String subject,
        String body) {
}
