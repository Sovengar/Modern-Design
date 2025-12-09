package jonathan.modern_design.amazon.inventory.queries;

import jonathan.modern_design._shared.tags.adapters.WebAdapter;
import jonathan.modern_design.amazon.inventory.domain.Stock;
import jonathan.modern_design.amazon.inventory.domain.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.InventoryUrls.INVENTORY_MODULE_URL;
import static jonathan.modern_design._shared.infra.AppUrls.AmazonUrls.InventoryUrls.STOCK_RESOURCE_URL;

@WebAdapter(INVENTORY_MODULE_URL + STOCK_RESOURCE_URL)
@RequiredArgsConstructor
public class GetStock {
    private final StockRepo stockRepo;

    @GetMapping("/{sku}")
    @Transactional
    public Integer handle(@PathVariable String sku) {
        return stockRepo.findById(sku).map(Stock::getQuantityOnHand).orElse(0);
    }
}
