package jonathan.modern_design._shared.domain;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.List;

//Or extend from AbstractAggregateRoot<T> from Spring
public abstract class AbstractAggregateRoot {
    private final List<Object> domainEvents = new ArrayList<>();

    protected void registerEvent(Object event) {
        domainEvents.add(event);
    }

    //Mark the event to make it visible to Spring.
    //If inside a transaction, the events are published after the transaction is committed.
    @DomainEvents
    public List<Object> domainEvents() {
        return List.copyOf(domainEvents);
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }
}
