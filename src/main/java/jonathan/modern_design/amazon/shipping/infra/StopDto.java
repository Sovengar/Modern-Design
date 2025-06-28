package jonathan.modern_design.amazon.shipping.infra;

import jonathan.modern_design.amazon.shipping.domain.Stop;

record StopDto(int stopId, int sequence, Stop.Status status) {
}
