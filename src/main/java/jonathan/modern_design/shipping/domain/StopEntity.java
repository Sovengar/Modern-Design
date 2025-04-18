package jonathan.modern_design.shipping.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

//COMMNENTED BECAUSE IS NOT CREATED ON THE SCHEMA @Entity
@Getter
@Setter
public class StopEntity {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequence;

    @Enumerated(EnumType.STRING)
    private StopStatus status;

    //@ManyToOne(fetch = FetchType.LAZY)
    private ShipmentEntity shipment;

}
