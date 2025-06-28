package jonathan.modern_design.amazon.catalog;

import jonathan.modern_design._shared.tags.Facade;
import jonathan.modern_design.amazon.catalog.domain.Product;
import jonathan.modern_design.amazon.catalog.domain.ProductRepo;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Facade
@RequiredArgsConstructor
public class CatalogApi {
    private final ProductRepo productRepo;

    public Map<String, Double> getManyPrices(Collection<String> ids) {
        return productRepo.findAllById(ids).stream().collect(toMap(Product::getSku, Product::getPrice));
    }
}
