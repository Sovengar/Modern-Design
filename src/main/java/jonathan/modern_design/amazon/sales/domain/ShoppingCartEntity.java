package jonathan.modern_design.amazon.sales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;

@Entity
@Table(name = "shopping_carts", schema = "sales")
@Getter
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(access = PACKAGE)
@Builder //Allowed because is a class without biz logic, use only for mapping or testing purposes
public class ShoppingCartEntity extends AbstractAggregateRoot<ShoppingCartEntity> {
    @Id
    @Column(name = "shopping_cart_id")
    private UUID id;
    private String customerId;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_cart_id")
    private List<ShoppingCartItem> items = new ArrayList<>();

    //TODO MIGRATE EVENTS TO THIS CLASS
}
