package jonathan.modern_design.amazon.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_reviews", schema = "catalog")
@SequenceGenerator(name = "product_reviews_SQ", schema = "catalog", sequenceName = "product_reviews_SQ", allocationSize = 1)
public class ProductReview {
    @Id
    @GeneratedValue(generator = "product_reviews_SQ")
    @Column(name = "product_review_id")
    private Long id;
    private String title;
    private String contents;
    private Double stars;
    private LocalDateTime createdAt;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Product product;

    public Optional<Double> stars() {
        return Optional.ofNullable(stars);
    }

    public Optional<String> title() {
        return Optional.ofNullable(title);
    }
}
