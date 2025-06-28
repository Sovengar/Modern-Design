package jonathan.modern_design.amazon.catalog.application;


import jonathan.modern_design.amazon.catalog.domain.Product;
import jonathan.modern_design.amazon.catalog.domain.ProductRepo;
import jonathan.modern_design.amazon.catalog.domain.ProductReview;
import jonathan.modern_design.amazon.catalog.domain.ProductReviewRepo;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddReview {
    private final ProductRepo productRepo;
    private final ProductReviewRepo productReviewRepo;

    @PostMapping("catalog/{sku}/reviews")
    @Transactional
    public void handle(@PathVariable String sku, @RequestBody AddReviewRequest request) {
        Product product = productRepo.findById(sku).orElseThrow();
        ProductReview review = new ProductReview(null, request.title(), request.contents(), request.stars(), LocalDateTime.now(), product);

        // CURRENT reviews & stars -> Product
        product.getReviews().add(review);
        product.setStars(computeAverageStars(product.getReviews()));

        // NEXT: reviews & stars -> ReviewedProduct
        // 1) find or create a ReviewedProduct
        // 2) do the same as CURRENT above

        productReviewRepo.save(review);
    }

    private Double computeAverageStars(List<ProductReview> reviews) {
        return reviews
                .stream()
                .flatMap(productReview -> productReview.stars().stream())
                .mapToDouble(Double::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }

    @Builder
    public record AddReviewRequest(
            String title,
            String contents,
            Double stars
    ) {
    }
}
