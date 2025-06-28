package jonathan.modern_design.amazon.order;

import java.util.UUID;

public record OrderStatusChanged(
        UUID orderId,
        OrderStatus status,
        String customerId
) {
}
