package jonathan.modern_design.amazon.catalog.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepo extends JpaRepository<ProductReview, Long> {
}
