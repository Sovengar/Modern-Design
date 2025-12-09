package jonathan.modern_design.amazon.shipping.application;

import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.amazon.shipping.api.ShippingResultResolved;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.ShippingUrls.SHIPPING_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.ShippingUrls.SHIPPING_RESOURCE_URL;

@WebAdapter(SHIPPING_MODULE_URL + SHIPPING_RESOURCE_URL)
@RequiredArgsConstructor
public class AckShipping { //ShippingProviderWebHookApi {
    private final ApplicationEventPublisher eventPublisher;

    @PutMapping("/{orderId}/status")
    @Transactional
    public String call(@PathVariable UUID orderId, @RequestBody boolean ok) {
        eventPublisher.publishEvent(new ShippingResultResolved(orderId, ok));
        return "Shipping callback received";
    }
}
