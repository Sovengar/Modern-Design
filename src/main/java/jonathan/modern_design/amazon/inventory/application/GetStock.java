package jonathan.modern_design.amazon.inventory.application;

import jonathan.modern_design.amazon.inventory.domain.Stock;
import jonathan.modern_design.amazon.inventory.domain.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetStock {
    private final StockRepo stockRepo;

    @GetMapping("stock/{sku}")
    @Transactional
    public Integer handle(@PathVariable String sku) {
        return stockRepo.findById(sku).map(Stock::getQuantityOnHand).orElse(0);
    }
}
