package jonathan.modern_design._shared.domain;

import jonathan.modern_design._shared.domain.models.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepo {
    List<Country> countries();

    Optional<Country> findByCode(String code);

    default Country findByCodeOrElseThrow(String code) {
        return findByCode(code).orElseThrow(() -> new IllegalArgumentException("Country not found"));
    }
}
