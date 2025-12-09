package jonathan.modern_design.amazon.order;

import jonathan.modern_design.amazon.order.application.PlaceOrder;
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
    private final PlaceOrder placeOrder;

    @Transactional
    public void complete(Order order) {
        //TODO order.complete();
        events.publishEvent(new OrderCompleted(order.getId()));
    }

    public String placeOrder(PlaceOrder.PlaceOrderRequest request) {
        return placeOrder.handle(request);
    }
}
