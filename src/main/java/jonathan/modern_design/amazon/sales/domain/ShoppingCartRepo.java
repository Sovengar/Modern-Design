package jonathan.modern_design.amazon.sales.domain;

import java.util.UUID;

public interface ShoppingCartRepo {
    ShoppingCart getById(UUID id);

    void save(ShoppingCart shoppingCart);
}
