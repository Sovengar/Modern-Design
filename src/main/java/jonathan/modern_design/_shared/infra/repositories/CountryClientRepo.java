package jonathan.modern_design._shared.infra.repositories;

import jonathan.modern_design._shared.domain.CountryRepo;
import jonathan.modern_design._shared.domain.models.Country;
import jonathan.modern_design._shared.tags.adapters.DataAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@DataAdapter
class CountryClientRepo implements CountryRepo {
    private final RestTemplate restTemplate;

    @Value("${thecompaniesapi.base-uri}")
    private String theCompaniesApiBaseUri;

    public CountryClientRepo(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<Country> countries() {
        List<Country> countries = new ArrayList<>();
        int currentPage = 1;
        ClientResponse.TheCompaniesAPIResponse response;

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

    private List<Country> convertTheCompaniesAPIResponseToCountries(ClientResponse.TheCompaniesAPIResponse response) {
        return response.results().stream()
                .map(toCountry())
                .toList();
    }

    private Function<ClientResponse.Country, Country> toCountry() {
        return theCompaniesAPICountry ->
                new Country(
                        theCompaniesAPICountry.code(),
                        theCompaniesAPICountry.name());
    }

    private ClientResponse.TheCompaniesAPIResponse getCountriesFromTheCompaniesAPI(String url) {
        return restTemplate.getForObject(url, ClientResponse.TheCompaniesAPIResponse.class);
    }

    static class ClientResponse {
        record Continent(
                String code,
                String latitude,
                String longitude,
                String name,
                String nameEs,
                String nameFr
        ) {
        }

        record Country(
                String code,
                Continent continent,
                String latitude,
                String longitude,
                String name,
                String nameEs,
                String nameFr,
                String nameNative,
                int population
        ) {
        }

        record Meta(
                int currentPage,
                int firstPage,
                int lastPage,
                int perPage,
                int total
        ) {
        }

        record TheCompaniesAPIResponse(
                List<Country> results,
                Meta meta
        ) {
        }
    }
}
