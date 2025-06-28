package jonathan.modern_design.amazon.shipping.application;

import jonathan.modern_design.amazon.shipping.api.ShippingResultResolved;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AckShipping { //ShippingProviderWebHookApi {
    private final ApplicationEventPublisher eventPublisher;

    @PutMapping("shipping/{orderId}/status")
    @Transactional
    public String call(@PathVariable UUID orderId, @RequestBody boolean ok) {
        eventPublisher.publishEvent(new ShippingResultResolved(orderId, ok));
        return "Shipping callback received";
    }
}
