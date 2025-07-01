package jonathan.modern_design.amazon.sales.domain;

import jonathan.modern_design.amazon.sales.domain.events.ShoppingCartCreated;
import jonathan.modern_design.amazon.sales.domain.events.ShoppingCartItemAdded;
import jonathan.modern_design.amazon.sales.domain.events.ShoppingCartItemQuantityIncreased;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ShoppingCart {
    final List<Object> events = new ArrayList<>();
    @Getter
    private final ShoppingCartEntity shoppingCartEntity;

    public ShoppingCart(ShoppingCartEntity shoppingCartEntity) {
        this.shoppingCartEntity = shoppingCartEntity;
    }

    public static ShoppingCart create(UUID shoppingCartId, String customerId) {
        var shoppingCartEntity = new ShoppingCartEntity(shoppingCartId, customerId, new ArrayList<>());
        var shoppingCart = new ShoppingCart(shoppingCartEntity);
        shoppingCart.events.add(new ShoppingCartCreated(shoppingCartId));
        return shoppingCart;
    }

    public void addItem(UUID productId, int quantity, BigDecimal price) {
        var existingItem = shoppingCartEntity.getItems().stream().filter(item -> item.productId == productId).findFirst().orElse(null);

        if (Objects.nonNull(existingItem)) {
            existingItem.quantity += quantity;
            events.add(new ShoppingCartItemQuantityIncreased(productId, quantity));
        } else {
            shoppingCartEntity.items.add(new ShoppingCartItem(UUID.randomUUID(), shoppingCartEntity.id, productId, quantity, price));
            events.add(new ShoppingCartItemAdded(productId, quantity, price));
        }
    }
}
