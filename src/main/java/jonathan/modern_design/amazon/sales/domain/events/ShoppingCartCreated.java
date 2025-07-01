package jonathan.modern_design.amazon.sales.domain.events;

import java.util.UUID;

public record ShoppingCartCreated(UUID shoppingCartId) {
}
