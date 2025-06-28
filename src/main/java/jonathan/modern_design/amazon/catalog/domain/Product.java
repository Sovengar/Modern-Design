package jonathan.modern_design.amazon.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products", schema = "catalog")
public class Product {
    @Id
    @Column(name = "sku_id")
    private String sku;
    private String name;
    private String description;
    //TODO QUITAR ESTOS, NO PERTENECEN AQUI
    private Double price;
    private Double stars;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews = new ArrayList<>();
}
