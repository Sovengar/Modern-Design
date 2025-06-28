package jonathan.modern_design.amazon.order.application;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jonathan.modern_design.amazon.catalog.CatalogApi;
import jonathan.modern_design.amazon.inventory.api.InventoryApi;
import jonathan.modern_design.amazon.inventory.domain.LineItem;
import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderRepo;
import jonathan.modern_design.amazon.payment.PaymentApi;
import jonathan.modern_design.amazon.shipping.api.ShippingResultResolved;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceOrder {
    private final OrderRepo orderRepo;
    private final CatalogApi catalogApi;
    private final InventoryApi inventoryApi;
    private final PaymentApi paymentApi;

    @PostMapping("order")
    public String handle(@RequestBody @Validated PlaceOrderRequest request) {
        Map<String, Integer> items = request.items.stream().collect(toMap(LineItem::productId, LineItem::count));
        Order order = new Order(request.orderId(), request.customerId(), request.shippingAddress(), null, items);

        orderRepo.save(order);
        inventoryApi.reserveStock(order.getId(), request.items);
        return paymentApi.generatePaymentUrl(order.getId(), getTotalPrice(request));
    }

    private double getTotalPrice(final PlaceOrderRequest request) {
        List<String> productIds = request.items().stream().map(LineItem::productId).toList();
        Map<String, Double> prices = catalogApi.getManyPrices(productIds);
        return request.items.stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    }

    @ApplicationModuleListener
//  @EventListener
    public void onShippingResultEvent(ShippingResultResolved event) {
        log.info("Listened to {}", event);
        Order order = orderRepo.findById(event.orderId()).orElseThrow();
        order.wasShipped(event.ok());
        orderRepo.save(order); // without @Test fails, as events not published
        log.info("Listener completed");
    }

    public record PlaceOrderRequest(
            @NotNull UUID orderId,
            @NotEmpty String customerId,
            @NotEmpty List<LineItem> items,
            @NotEmpty String shippingAddress) {
    }
}
