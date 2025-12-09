package jonathan.modern_design.amazon.catalog.queries;

import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.amazon.catalog.domain.Product;
import jonathan.modern_design.amazon.catalog.domain.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.CatalogUrls.CATALOG_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.CatalogUrls.CATALOG_RESOURCE_URL;

@Slf4j
@WebAdapter(CATALOG_MODULE_URL + CATALOG_RESOURCE_URL)
@RequiredArgsConstructor
public class GetProduct {
    private final ProductRepo productRepo;

    @GetMapping("/{sku}")
    public GetProductResponse call(@PathVariable String sku) {
        Product product = productRepo.findById(sku).orElseThrow();
        int stock = 0; // TODO display stock in product page UI
        return new GetProductResponse(product.getSku(),
                product.getName(),
                product.getDescription(),
                stock,
                product.getPrice(),
                product.getStars()
        );
    }

    public record GetProductResponse(
            String sku,
            String name,
            String description,
            int stock,
            Double price,
            Double stars
    ) {
    }
}
// Hints:
// 1. stock is in inventory/impl/Stock#items
// 2. ▶️ GetProductApiTest ✅
// 3. ▶️ ArchitectureTest ✅ (uses repositories-modulith)
//    by default a module is only allowed to depend on classes
//    in the top-level package of another module
