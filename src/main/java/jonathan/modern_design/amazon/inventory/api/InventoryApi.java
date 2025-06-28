package jonathan.modern_design.amazon.inventory.api;

import jonathan.modern_design.amazon.inventory.domain.LineItem;
import jonathan.modern_design.amazon.inventory.domain.Stock;
import jonathan.modern_design.amazon.inventory.domain.StockRepo;
import jonathan.modern_design.amazon.inventory.domain.StockReservation;
import jonathan.modern_design.amazon.inventory.domain.StockReservationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryApi {
    private final StockService stockService;
    private final StockRepo stockRepo;

    public void reserveStock(long orderId, List<LineItem> items) {
        stockService.reserveStock(orderId, items);
    }

    public void confirmReservation(long orderId) {
        stockService.confirmReservation(orderId);
    }
}

@Slf4j
@Service
@RequiredArgsConstructor
class StockService {
    private final StockRepo stockRepo;
    private final StockReservationRepo stockReservationRepo;

    @Transactional
    public void reserveStock(long orderId, List<LineItem> items) {
        for (var item : items) {
            subtractStock(item.productId(), item.count());
            createReservation(orderId, item.productId(), item.count());
        }
    }

    private void createReservation(long orderId, String productId, Integer count) {
        StockReservation reservation = new StockReservation(orderId, productId, count);
        stockReservationRepo.save(reservation);
    }

    private void subtractStock(String productId, Integer count) {
        Stock stock = stockRepo.findById(productId).orElseThrow();
        stock.remove(count);
        stockRepo.save(stock);
    }

    @Transactional
    public void confirmReservation(long orderId) {
        log.info("Stock reservation confirmed for order {}", orderId);
        stockReservationRepo.deleteAllByOrderId(orderId);
    }

}
