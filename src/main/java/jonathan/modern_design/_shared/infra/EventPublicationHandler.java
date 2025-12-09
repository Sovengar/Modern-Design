package jonathan.modern_design._shared.infra;

import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebAdapter
@RequiredArgsConstructor
public class EventPublicationHandler {
    private final JdbcTemplate jdbcTemplate;
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    @GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(0L); // No timeout
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        // Enviar los últimos 20 eventos históricos
        List<Map<String, Object>> lastEvents = jdbcTemplate.queryForList(
                "SELECT * FROM md.event_publication ORDER BY id DESC LIMIT 20"
        );
        Collections.reverse(lastEvents); // Para mostrar en orden cronológico

        try {
            for (Map<String, Object> event : lastEvents) {
                emitter.send(event, MediaType.APPLICATION_JSON);
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(emitter);
        }

        return emitter;
    }

    @EventListener
    public void onAnyEvent(Object event) {
        // Filtrar eventos internos de Spring (por package)
        String pkg = event.getClass().getPackage() != null ? event.getClass().getPackage().getName() : "";
        if (pkg.startsWith("org.springframework")) return;

        // Consultar el último registro insertado en event_publication
        List<Map<String, Object>> lastEvents = jdbcTemplate.queryForList(
                "SELECT * FROM md.event_publication ORDER BY id DESC LIMIT 1"
        );
        if (!lastEvents.isEmpty()) {
            Map<String, Object> eventMap = lastEvents.getFirst();
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(eventMap, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    emitters.remove(emitter);
                }
            }
        }
    }
}
