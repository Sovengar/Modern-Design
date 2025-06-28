package jonathan.modern_design.amazon.payment.application;

import jonathan.modern_design.amazon.inventory.api.InventoryApi;
import jonathan.modern_design.amazon.order.OrderStatus;
import jonathan.modern_design.amazon.order.domain.Order;
import jonathan.modern_design.amazon.order.domain.OrderRepo;
import jonathan.modern_design.amazon.shipping.api.ShippingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class Pay { //PaymentGatewayWebHookApi {
    private final OrderRepo orderRepo;
    private final ShippingApi shippingApi;
    private final InventoryApi inventoryApi;

    @PutMapping("payment/{orderId}/status")
    public String confirmPayment(@PathVariable UUID orderId, @RequestBody boolean ok) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.pay(ok);
        if (order.getStatus() == OrderStatus.PAYMENT_APPROVED) {
            inventoryApi.confirmReservation(order.getId());
            String trackingNumber = shippingApi.requestShipment(order.getId(), order.getShippingAddress());
            order.wasScheduleForShipping(trackingNumber);
        }
        orderRepo.save(order);
        return "Payment callback received";
    }
}
