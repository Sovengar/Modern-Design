package jonathan.modern_design.amazon.shipping.api;

import java.util.UUID;

public record ShippingResultResolved(UUID orderId, boolean ok) {
}
