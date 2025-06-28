package jonathan.modern_design.amazon.shipping.domain;

import java.util.List;
import java.util.UUID;

public interface StopRepo {
    List<StopEntity> findStopDataByShipmentId(UUID shipmentId);
}
