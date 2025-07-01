package jonathan.modern_design.amazon.sales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart_item", schema = "sales")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem {
    @Id
    @Column(name = "shopping_cart_item_id")
    UUID internalId; //UUID instead of a sequence for simplicity
    UUID shoppingCartId;
    UUID productId;
    int quantity;
    BigDecimal price;

    //@ManyToOne
    //@JoinColumn(name = "shopping_cart_id")
    //ShoppingCartEntity shoppingCartEntity;
}
