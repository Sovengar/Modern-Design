package jonathan.modern_design._shared.country;

import java.util.List;
import java.util.Optional;

public interface CountriesInventory {
    List<Country> countries();

    Optional<Country> findByCode(String code);

    default Country findByCodeOrElseThrow(String code) {
        return findByCode(code).orElseThrow(() -> new IllegalArgumentException("Country not found"));
    }
}
