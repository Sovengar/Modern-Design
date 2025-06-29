package jonathan.modern_design._shared.infra.country;

import jonathan.modern_design._shared.domain.CountriesCatalog;
import jonathan.modern_design._shared.domain.Country;
import jonathan.modern_design._shared.tags.Injectable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Injectable
class TheCompaniesAPIClient implements CountriesCatalog {
    private final RestTemplate restTemplate;

    @Value("${thecompaniesapi.base-uri}")
    private String theCompaniesApiBaseUri;

    public TheCompaniesAPIClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<Country> countries() {
        List<Country> countries = new ArrayList<>();
        int currentPage = 1;
        TheCompaniesAPIWrapper.TheCompaniesAPIResponse response;

        do {
            String url = theCompaniesApiBaseUri + "?page=" + currentPage;
            response = getCountriesFromTheCompaniesAPI(url);

            if (Objects.isNull(response)) {
                break;
            }

            countries.addAll(convertTheCompaniesAPIResponseToCountries(response));
            currentPage++;
        } while (currentPage <= response.meta().lastPage());

        return countries;
    }

    @Override
    public Optional<Country> findByCode(final String code) {
        return Optional.of(new Country(code, code)); //Here we should call the api to get the single country
    }

    private List<Country> convertTheCompaniesAPIResponseToCountries(TheCompaniesAPIWrapper.TheCompaniesAPIResponse response) {
        return response.results().stream()
                .map(toCountry())
                .toList();
    }

    private Function<TheCompaniesAPIWrapper.Country, Country> toCountry() {
        return theCompaniesAPICountry ->
                new Country(
                        theCompaniesAPICountry.code(),
                        theCompaniesAPICountry.name());
    }

    private TheCompaniesAPIWrapper.TheCompaniesAPIResponse getCountriesFromTheCompaniesAPI(String url) {
        return restTemplate.getForObject(url, TheCompaniesAPIWrapper.TheCompaniesAPIResponse.class);
    }
}
