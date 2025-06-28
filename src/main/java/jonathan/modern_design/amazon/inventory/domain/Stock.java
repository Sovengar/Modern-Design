package jonathan.modern_design.amazon.inventory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@Table(schema = "inventory")
public class Stock {
    @Id
    @Column(name = "sku_id")
    private String sku;

    @NotNull
    private Integer quantityOnHand = 0;

    private String location;

    public Stock(String sku, Integer quantityOnHand, String location) {
        this.sku = sku;
        this.quantityOnHand = quantityOnHand;
        this.location = location;
    }

    //TODO
    //shipProduct
    //receiveProduct
    //adjustInventory

    public Stock add(int itemsAdded) {
        if (itemsAdded <= 0) {
            throw new IllegalArgumentException("Negative: " + itemsAdded);
        }
        quantityOnHand += itemsAdded;
        return this;
    }

    public void remove(Integer itemsRemoved) {
        if (itemsRemoved <= 0) {
            throw new IllegalArgumentException("Negative: " + itemsRemoved);
        }
        if (itemsRemoved > quantityOnHand) {
            throw new IllegalArgumentException("Not enough stock to remove: " + itemsRemoved);
        }
        quantityOnHand -= itemsRemoved;
    }
}
