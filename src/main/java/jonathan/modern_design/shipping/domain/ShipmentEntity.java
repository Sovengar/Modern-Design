package jonathan.modern_design.shipping.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

//COMMNENTED BECAUSE IS NOT CREATED ON THE SCHEMA @Entity
@Getter
@Setter
public class ShipmentEntity {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //@OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StopEntity> stops;

    // Otros campos de persistencia según necesidades
}
