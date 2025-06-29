package jonathan.modern_design.notification;

import java.util.List;


record Email(
        String to,
        List<String> cc,
        String from,
        String subject,
        String body) {
}
