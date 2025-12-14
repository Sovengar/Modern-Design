package jonathan.modern_design._shared.country;

import jonathan.modern_design.__config.utils.PrettyTestNames;
import jonathan.modern_design._shared.domain.CountryRepo;
import jonathan.modern_design._shared.infra.repositories.CountryInMemoryRepo;
import lombok.val;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(PrettyTestNames.class)
class CountryRepoTest {

    //Atencion, este test no esta haciendo nada, pero sirve de ejemplo de como evitar llamar al servicio externo.
    @Test
    void should_retrieve_countries() {
        CountryRepo stub = new CountryInMemoryRepo();

        val countries = stub.countries();
        assertThat(countries).isNotEmpty();
    }
}



