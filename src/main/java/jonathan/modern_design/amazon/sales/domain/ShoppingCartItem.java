package jonathan.modern_design.amazon.sales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jonathan.modern_design._shared.tags.persistence.LinkedAsFKinDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart_item", schema = "sales")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ShoppingCartItem {
    @Id
    @Column(name = "shopping_cart_item_id")
    private UUID internalId; //UUID instead of a sequence for simplicity

    @LinkedAsFKinDB //Avoid bidirectional links
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;
    private UUID productId;
    private int quantity;
    private BigDecimal price;

    public void addItem(int quantity) {
        this.quantity += quantity;
    }
}
