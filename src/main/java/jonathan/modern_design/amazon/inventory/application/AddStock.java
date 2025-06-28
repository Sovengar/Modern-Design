package jonathan.modern_design.amazon.inventory.application;

import jonathan.modern_design.amazon.inventory.domain.Stock;
import jonathan.modern_design.amazon.inventory.domain.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddStock {
    private final StockRepo stockRepo;

    @PostMapping("stock/{sku}/add/{quantity}")
    @Transactional
    public void handle(@PathVariable String sku, @PathVariable int quantity) {
        var stock = stockRepo.findById(sku).orElse(new Stock(sku, 0, ""));
        stock.add(quantity);
        stockRepo.save(stock);
    }
}
