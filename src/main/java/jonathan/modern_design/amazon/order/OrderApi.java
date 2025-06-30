package jonathan.modern_design.amazon.order;

import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderCompleted;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApi {
    private final ApplicationEventPublisher events;

    @Transactional
    public void complete(Order order) {
        events.publishEvent(new OrderCompleted(order.getId()));
    }
}
