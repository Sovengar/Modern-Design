package jonathan.modern_design.amazon.sales.domain.events;

import java.math.BigDecimal;
import java.util.UUID;

public record ShoppingCartItemAdded(UUID productId, int quantity, BigDecimal price) {
}
