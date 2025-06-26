package jonathan.modern_design.amazon.shipping.domain;

import java.util.ArrayList;
import java.util.List;

//OrderAR
/*
public Shipment Ship(LocalDateTime expectedPickup, LocalDateTime expectedDelivery) {
    var pickup = new PickupStop(expectedPickup);
    var delivery = new DeliveryStop(expectedDelivery);
    return new Shipment.Factory(pickup, delivery);
}

 */

public class Shipment {

    private List<StopEntity> stopsEntity = new ArrayList<>();
    private List<Stop> stops = new ArrayList<>();

    private Shipment(List<Stop> stops) {
        this.stops = stops;
    }

    public boolean isComplete() {
        return stopsEntity.stream().allMatch(stop -> stop.getStatus() == Stop.Status.DEPARTED);
    }

    public void arrive(int stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        boolean previousAreNotDeparted = stopsEntity.stream()
                .anyMatch(stop -> stop.getStatus() != Stop.Status.DEPARTED); //add stop.sequence() < currentStop.sequence() &&

        if (previousAreNotDeparted) {
            throw new IllegalStateException("Previous stops have not departed");
        }

        if (currentStop.getStatus() != Stop.Status.IN_TRANSIT) {
            throw new IllegalStateException("Current stop is not in transit");
        }

        currentStop.arrive();
    }

    public void pickup(int stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        if (!(currentStop instanceof PickupStop)) {
            throw new IllegalStateException("Stop is not a pickup.");
        }

        currentStop.depart();
    }

    public void deliver(int stopId) {
        var currentStop = getCurrentStopOrElseThrow(stopId);

        if (!(currentStop instanceof DeliveryStop)) {
            throw new IllegalStateException("Stop is not a delivery.");
        }

        currentStop.depart();
    }

    private StopEntity getCurrentStopOrElseThrow(final Long stopId) {
        return stopsEntity.stream()
                .filter(stop -> stop.getId().equals(stopId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stop not found"));
    }

    private Stop getCurrentStopOrElseThrow(final int stopId) {
        return stops.stream()
                .filter(stop -> stop.getStopId() == stopId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stop not found"));
    }

    public static class Factory {
        public static Shipment create(PickupStop pickup, DeliveryStop delivery) {
            return new Shipment(List.of(pickup, delivery));
        }

        public static Shipment create(List<Stop> stops) {
            if (stops.size() < 2) {
                throw new IllegalArgumentException("Shipment must have at least 2 stops.");
            }

            if (!(stops.getFirst() instanceof PickupStop)) {
                throw new IllegalArgumentException("First stop must be a pickup.");
            }

            if (!(stops.getLast() instanceof DeliveryStop)) {
                throw new IllegalArgumentException("Last stop must be a delivery.");
            }

            return new Shipment(stops);
        }

        public static Shipment createWithEntity(List<StopEntity> stopsEntity) {
            return null;
            //TODO return new Shipment(stopsEntity.toDomain());
        }
    }
}
