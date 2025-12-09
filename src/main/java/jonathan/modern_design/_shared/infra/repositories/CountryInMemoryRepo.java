package jonathan.modern_design._shared.infra.repositories;

import jonathan.modern_design._shared.domain.CountryRepo;
import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.tags.tests.StubInProduction;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@StubInProduction
public final class CountryInMemoryRepo implements CountryRepo {
    private static final List<Country> DEFAULT_COUNTRIES = asList(
            new Country("ES", "Spain"),
            new Country("US", "United States"),
            new Country("GB", "United Kingdom"),
            new Country("DE", "Germany"),
            new Country("FR", "France"),
            new Country("IT", "Italy"),
            new Country("RU", "Russia"),
            new Country("CN", "China"),
            new Country("JP", "Japan"),
            new Country("IN", "India"),
            new Country("BR", "Brazil"),
            new Country("MX", "Mexico"),
            new Country("CA", "Canada"),
            new Country("AU", "Australia"),
            new Country("AR", "Argentina"),
            new Country("CO", "Colombia"),
            new Country("CL", "Chile"),
            new Country("PE", "Peru"),
            new Country("EC", "Ecuador"),
            new Country("VE", "Venezuela"),
            new Country("UY", "Uruguay"),
            new Country("BO", "Bolivia"),
            new Country("CH", "Switzerland"));

    private final List<Country> countries;

    public CountryInMemoryRepo() {
        countries = DEFAULT_COUNTRIES;
    }

    public CountryInMemoryRepo(List<Country> countries) {
        this.countries = countries;
    }

    @Override
    public List<Country> countries() {
        return countries;
    }

    @Override
    public Optional<Country> findByCode(final String code) {
        return countries.stream().filter(c -> code.equals(c.code())).findFirst();
    }
}
