package jonathan.modern_design._shared.event_publication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.core.DefaultEventPublicationRegistry;
import org.springframework.modulith.events.core.TargetEventPublication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
class A {
    private final DefaultEventPublicationRegistry registry;

    @Scheduled(fixedDelay = 4000)
    public void publish() {
        log.info("Publishing event");
        Collection<? extends TargetEventPublication> all = registry.findAll();
        log.info("Found {} publications", all.size());
    }

}
