package com.jonathan.modern_design.shared.country;

import com.jonathan.modern_design.config.PrettyTestNames;
import lombok.val;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(PrettyTestNames.class)
class CountriesInventoryTest {

    //Atencion, este test no esta haciendo nada, pero sirve de ejemplo de como evitar llamar al servicio externo.
    @Test
    void should_retrieve_countries() {
        CountriesInventory stub = new CountriesInventoryStub();

        val countries = stub.countries();
        assertThat(countries).isNotEmpty();
    }
}



