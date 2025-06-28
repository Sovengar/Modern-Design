package jonathan.modern_design.amazon.inventory.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// ⚠️ The very last resort!
//   Use only when you really have to join this
//   to a query of another module to avoid N+1 queries
@Entity
@Table(schema = "INVENTORY")
@View(query = """
        select STOCK.sku_id, STOCK.quantity_on_hand as STOCK
        from INVENTORY.STOCK
        """)
@Getter
@Immutable // cannot be INSERT/UPDATE/DELETE
public class StockView { // part of the PUBLIC API of Inventory Module
    // these fields are the contract of Inventory Modules
    @Id
    private String skuId;
    private Integer stock;
}
