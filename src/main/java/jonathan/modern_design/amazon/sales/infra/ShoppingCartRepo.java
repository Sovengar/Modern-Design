package jonathan.modern_design.amazon.sales.infra;

import jonathan.modern_design.amazon.sales.domain.ShoppingCart;

import java.util.UUID;

interface ShoppingCartRepo {
    ShoppingCart getById(UUID id);

    void save(ShoppingCart shoppingCart);
}
