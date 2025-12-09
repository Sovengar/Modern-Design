package jonathan.modern_design.amazon.sales.infra;

import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import jonathan.modern_design.amazon.sales.domain.ShoppingCart;
import jonathan.modern_design.amazon.sales.domain.ShoppingCartRepo;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@DataAdapter
@RequiredArgsConstructor
class ShoppingCartPostgreRepo implements ShoppingCartRepo {
    private final ShoppingCartSpringJpaRepo jpaRepo;

    @Override
    public ShoppingCart getById(final UUID id) {
        var shoppingCartEntity = jpaRepo.findById(id).orElseThrow();
        return new ShoppingCart(shoppingCartEntity);
    }

    @Override
    public void save(final ShoppingCart shoppingCart) {
        jpaRepo.save(shoppingCart.getShoppingCartEntity());
    }
}
